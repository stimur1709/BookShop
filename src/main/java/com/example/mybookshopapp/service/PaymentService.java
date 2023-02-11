package com.example.mybookshopapp.service;

import com.example.mybookshopapp.config.PaymentConfig;
import com.example.mybookshopapp.data.dto.YKassa.Amount;
import com.example.mybookshopapp.data.dto.YKassa.Confirmation;
import com.example.mybookshopapp.data.dto.YKassa.Payment;
import com.example.mybookshopapp.data.dto.YKassa.PaymentRequest;
import com.example.mybookshopapp.data.entity.book.Book;
import com.example.mybookshopapp.data.entity.config.Api;
import com.example.mybookshopapp.data.entity.payments.BalanceTransaction;
import com.example.mybookshopapp.data.entity.payments.BalanceTransactionDto;
import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.repository.BalanceTransactionRepository;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class PaymentService {

    private final RestTemplate restTemplate;
    private final PaymentConfig paymentConfig;
    private final BookRepository bookRepository;
    private final BalanceTransactionRepository balanceTransactionRepository;
    private final UserProfileService userProfileService;
    private final ModelMapper modelMapper;
    private final LocaleResolver localeResolver;
    private final HttpServletRequest request;

    @Autowired
    public PaymentService(RestTemplate restTemplate, PaymentConfig paymentConfig, BookRepository bookRepository, BalanceTransactionRepository balanceTransactionRepository, UserProfileService userProfileService, ModelMapper modelMapper, LocaleResolver localeResolver, HttpServletRequest request) {
        this.restTemplate = restTemplate;
        this.paymentConfig = paymentConfig;
        this.bookRepository = bookRepository;
        this.balanceTransactionRepository = balanceTransactionRepository;
        this.userProfileService = userProfileService;
        this.modelMapper = modelMapper;
        this.localeResolver = localeResolver;
        this.request = request;
    }

    public String getPaymentUrl(String amount, String description) {
        Api api = paymentConfig.getPayment();
        String uuid = UUID.randomUUID().toString();
        PaymentRequest request = new PaymentRequest(new Amount(amount), new Confirmation("redirect", api.getReturnUrl() + uuid), description);
        HttpEntity<PaymentRequest> httpEntity = new HttpEntity<>(request, getHeaders(api, uuid));
        ResponseEntity<Payment> exchange = restTemplate.exchange(api.getUrl(), HttpMethod.POST, httpEntity, Payment.class);
        Payment body = exchange.getBody();
        if (body != null) {
            createBalanceTransaction(body.getId(), uuid, amount);
            return body.getConfirmation().getConfirmationUrl();
        } else {
            return "/index";
        }
    }

    @Async
    @Transactional
    void createBalanceTransaction(String codePaymentEx, String codePaymentIn, String amount) {
        balanceTransactionRepository.save(new BalanceTransaction(userProfileService.getCurrentUser().getId(), Integer.parseInt(amount), codePaymentIn, codePaymentEx));
    }

    @Async
    @Transactional
    void createBalanceTransaction(UUID codePaymentIn, List<String> books) {
        List<BalanceTransaction> transactions = new ArrayList<>();
        for (Book book : bookRepository.findBookEntitiesBySlugIn(books)) {
            transactions.add(new BalanceTransaction(userProfileService.getCurrentUser().getId(), book.discountPrice(), book.getId(), codePaymentIn));
        }
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

    public Page<BalanceTransactionDto> getTransactionsUser() {
        Page<BalanceTransaction> transactions = balanceTransactionRepository.findByUserOrderByTimeDesc(userProfileService.getUserId(), PageRequest.of(0, 5));
        return mapper(transactions);
    }

    public List<BalanceTransactionDto> getTransactionsUser(int offset, int limit) {
        Page<BalanceTransaction> transactions = balanceTransactionRepository.findByUserOrderByTimeDesc(userProfileService.getUserId(), PageRequest.of(offset, limit));
        return mapper(transactions).getContent();
    }

    public Page<BalanceTransactionDto> mapper(Page<BalanceTransaction> transactions) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm", localeResolver.resolveLocale(request));
        transactions.getContent().forEach(balanceTransaction -> balanceTransaction.setFormatDate(simpleDateFormat));
        return transactions.map(balanceTransaction -> modelMapper.map(balanceTransaction, BalanceTransactionDto.class));
    }

    public RedirectView buyBooks(int amount, List<String> books) {
        User user = userProfileService.getCurrentUser();
        if (user.getBalance() >= amount) {
            createBalanceTransaction(UUID.randomUUID(), books);
            return new RedirectView("/my");
        }
        return new RedirectView("/profile/?difference=" + (amount - user.getBalance()) + "#topup");
    }
}
