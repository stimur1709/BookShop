package com.example.mybookshopapp.service;

import com.example.mybookshopapp.config.PaymentConfig;
import com.example.mybookshopapp.data.dto.YKassa.Amount;
import com.example.mybookshopapp.data.dto.YKassa.Confirmation;
import com.example.mybookshopapp.data.dto.YKassa.Payment;
import com.example.mybookshopapp.data.dto.YKassa.PaymentRequest;
import com.example.mybookshopapp.data.entity.book.Book;
import com.example.mybookshopapp.data.entity.payments.BalanceTransaction;
import com.example.mybookshopapp.repository.BalanceTransactionRepository;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
        HttpHeaders headers = (HttpHeaders) paymentConfig.getPayment().get("headers");
        String url = (String) paymentConfig.getPayment().get("url");
        PaymentRequest request = new PaymentRequest(new Amount(amount), new Confirmation("redirect", "http://localhost:8085/"), description);
        HttpEntity<PaymentRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<Payment> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Payment.class);
        Payment body = exchange.getBody();
        if (body != null) {
            createBalanceTransaction(body.getId(), books);
            return body.getConfirmation().getConfirmationUrl();
        } else {
            return "index";
        }
    }

    @Async
    @Transactional
    void createBalanceTransaction(String codePayment, List<String> books) {
        List<BalanceTransaction> transactions = new ArrayList<>();
        for (Book book : bookRepository.findBookEntitiesBySlugIn(books)) {
            transactions.add(new BalanceTransaction(userProfileService.getCurrentUser().getId(), book.discountPrice(), book.getId(), codePayment));
        }
        balanceTransactionRepository.saveAll(transactions);
    }

    @Async
    @Transactional
    @Scheduled(fixedDelay = 100000)
    void getStatusPayment() {
        log.info("Запрос статус платежей");
        HttpHeaders headers = (HttpHeaders) paymentConfig.getPayment().get("headers");
        String url = (String) paymentConfig.getPayment().get("url");
        List<String> transactions = balanceTransactionRepository.findDistinctByStatusPaymentIn(Arrays.asList(1, 2));
        HttpEntity<PaymentRequest> httpEntity = new HttpEntity<>(headers);
        for (String code : transactions) {
            ResponseEntity<Payment> exchange = restTemplate.exchange(url + '/' + code, HttpMethod.GET, httpEntity, Payment.class);
            int status = 1;
            if (exchange.getBody() != null) {
                switch (exchange.getBody().getStatus()) {
                    case "pending": {
                        status = 1;
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
}
