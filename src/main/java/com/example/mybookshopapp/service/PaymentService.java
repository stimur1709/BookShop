package com.example.mybookshopapp.service;

import com.example.mybookshopapp.config.PaymentConfig;
import com.example.mybookshopapp.data.entity.books.Book;
import com.example.mybookshopapp.data.entity.config.Api;
import com.example.mybookshopapp.data.entity.payments.BalanceTransaction;
import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.data.outher.YKassa.Amount;
import com.example.mybookshopapp.data.outher.YKassa.Confirmation;
import com.example.mybookshopapp.data.outher.YKassa.Payment;
import com.example.mybookshopapp.data.outher.YKassa.PaymentRequest;
import com.example.mybookshopapp.repository.BalanceTransactionRepository;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

@Service
@Slf4j
public class PaymentService {

    private final RestTemplate restTemplate;
    private final PaymentConfig paymentConfig;
    private final BookRepository bookRepository;
    private final BalanceTransactionRepository balanceTransactionRepository;
    private final UserProfileService userProfileService;
    private final MailService mailService;

    @Autowired
    public PaymentService(RestTemplate restTemplate, PaymentConfig paymentConfig, BookRepository bookRepository, BalanceTransactionRepository balanceTransactionRepository, UserProfileService userProfileService, MailService mailService) {
        this.restTemplate = restTemplate;
        this.paymentConfig = paymentConfig;
        this.bookRepository = bookRepository;
        this.balanceTransactionRepository = balanceTransactionRepository;
        this.userProfileService = userProfileService;
        this.mailService = mailService;
    }

    @Transactional
    public String getPaymentUrl(String amount, String description) {
        Api api = paymentConfig.getPayment();
        String uuid = UUID.randomUUID().toString();
        PaymentRequest request = new PaymentRequest(new Amount(amount), new Confirmation("redirect", api.getReturnUrl() + uuid), description);
        HttpEntity<PaymentRequest> httpEntity = new HttpEntity<>(request, getHeaders(api, uuid));
        ResponseEntity<Payment> exchange = restTemplate.exchange(api.getUrl(), HttpMethod.POST, httpEntity, Payment.class);
        Payment body = exchange.getBody();
        if (body != null) {
            String url = body.getConfirmation().getConfirmationUrl();
            mailService.sendMail(userProfileService.getCurrentUserDTO().getMail(), url, 2);
            createBalanceTransaction(body.getId(), uuid, amount);
            return url;
        } else {
            return "/index";
        }
    }

    @Async
    void createBalanceTransaction(String codePaymentEx, String codePaymentIn, String amount) {
        balanceTransactionRepository.save(new BalanceTransaction(userProfileService.getCurrentUser().getId(), Integer.parseInt(amount), codePaymentIn, codePaymentEx));
    }

    @Async
    @Transactional
    void createBalanceTransaction(UUID codePaymentIn, List<String> books) {
        List<BalanceTransaction> transactions = new ArrayList<>();
        List<Book> bookList = bookRepository.findBookEntitiesBySlugIn(books);
        for (Book book : bookList) {
            transactions.add(new BalanceTransaction(userProfileService.getCurrentUser().getId(), book.getDiscountPrice(), book.getId(), codePaymentIn));
        }
        mailService.sendMail(userProfileService.getCurrentUserDTO().getMail(), "http://localhost:8085/my", 3);
        balanceTransactionRepository.saveAll(transactions);
    }

    @Async
    @Transactional
    public void getStatusPaymentByUCodePaymentEx(String uuid) {
        List<String> transactions = balanceTransactionRepository.findDistinctByStatusPaymentInAndCodePaymentEx(Arrays.asList(1, 2), UUID.fromString(uuid));
        sendUrl(transactions);
    }

    @Async
    @Transactional
    @Scheduled(fixedDelay = 100000)
    void getStatusPayment() {
        log.info("Запрос статус платежей");
        List<String> transactions = balanceTransactionRepository.findDistinctByStatusPaymentIn(Arrays.asList(1, 2));
        sendUrl(transactions);
    }

    private void sendUrl(List<String> transactions) {
        Api api = paymentConfig.getPayment();
        HttpEntity<PaymentRequest> httpEntity = new HttpEntity<>(getHeaders(api));
        for (String code : transactions) {
            ResponseEntity<Payment> exchange = restTemplate.exchange(api.getUrl() + '/' + code, HttpMethod.GET, httpEntity, Payment.class);
            int status = 1;
            if (exchange.getBody() != null) {
                switch (exchange.getBody().getStatus()) {
                    case "pending": {
                        break;
                    }
                    case "waiting_for_capture": {
                        status = 2;
                        break;
                    }
                    case "succeeded": {
                        status = 3;
                        break;
                    }
                    case "canceled": {
                        status = 4;
                        break;
                    }
                }
                balanceTransactionRepository.updateStatusByCode(status, UUID.fromString(code));
            }
        }
    }

    private HttpHeaders getHeaders(Api api) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(api.getUsername(), api.getApiKey());
        return headers;
    }

    private HttpHeaders getHeaders(Api api, String uuid) {
        HttpHeaders headers = getHeaders(api);
        headers.add("Idempotence-Key", uuid);
        return headers;
    }

    @Transactional
    public RedirectView buyBooks(int amount, List<String> books) {
        User user = userProfileService.getCurrentUser();
        if (user.getBalance() >= amount) {
            createBalanceTransaction(UUID.randomUUID(), books);
            return new RedirectView("/my");
        }
        return new RedirectView("/profile/?difference=" + (amount - user.getBalance()) + "#topup");
    }
}
