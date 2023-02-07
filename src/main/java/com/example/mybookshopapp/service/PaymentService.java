package com.example.mybookshopapp.service;

import com.example.mybookshopapp.config.PaymentConfig;
import com.example.mybookshopapp.data.dto.YKassa.Amount;
import com.example.mybookshopapp.data.dto.YKassa.Confirmation;
import com.example.mybookshopapp.data.dto.YKassa.Payment;
import com.example.mybookshopapp.data.dto.YKassa.PaymentRequest;
import com.example.mybookshopapp.data.entity.book.Book;
import com.example.mybookshopapp.data.entity.config.Api;
import com.example.mybookshopapp.data.entity.payments.BalanceTransaction;
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

import java.util.*;

@Service
@Slf4j
public class PaymentService {

    private final RestTemplate restTemplate;
    private final PaymentConfig paymentConfig;
    private final BookRepository bookRepository;
    private final BalanceTransactionRepository balanceTransactionRepository;
    private final UserProfileService userProfileService;

    @Autowired
    public PaymentService(RestTemplate restTemplate, PaymentConfig paymentConfig, BookRepository bookRepository, BalanceTransactionRepository balanceTransactionRepository, UserProfileService userProfileService) {
        this.restTemplate = restTemplate;
        this.paymentConfig = paymentConfig;
        this.bookRepository = bookRepository;
        this.balanceTransactionRepository = balanceTransactionRepository;
        this.userProfileService = userProfileService;
    }

    public String getPaymentUrl(String amount, String description, List<String> books) {
        Api api = paymentConfig.getPayment();
        String uuid = UUID.randomUUID().toString();
        PaymentRequest request = new PaymentRequest(new Amount(amount), new Confirmation("redirect", api.getReturnUrl() + uuid), description);
        HttpEntity<PaymentRequest> httpEntity = new HttpEntity<>(request, getHeaders(api, uuid));
        ResponseEntity<Payment> exchange = restTemplate.exchange(api.getUrl(), HttpMethod.POST, httpEntity, Payment.class);
        Payment body = exchange.getBody();
        if (body != null) {
            createBalanceTransaction(body.getId(), uuid, books);
            return body.getConfirmation().getConfirmationUrl();
        } else {
            return "/index";
        }
    }

    @Async
    @Transactional
    void createBalanceTransaction(String codePaymentEx, String codePaymentIn, List<String> books) {
        List<BalanceTransaction> transactions = new ArrayList<>();
        for (Book book : bookRepository.findBookEntitiesBySlugIn(books)) {
            transactions.add(new BalanceTransaction(userProfileService.getCurrentUser().getId(), book.discountPrice(), book.getId(), codePaymentIn, codePaymentEx));
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
}
