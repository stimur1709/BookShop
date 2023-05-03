package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.BalanceTransactionDto;
import com.example.mybookshopapp.data.query.BTQuery;
import com.example.mybookshopapp.service.BalanceTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/transactions")
public class RestBalanceTransactionController
        extends RestDataControllerImpl<BTQuery, BalanceTransactionDto, BalanceTransactionDto, BalanceTransactionService> {

    @Autowired
    public RestBalanceTransactionController(BalanceTransactionService service) {
        super(service);
    }

    @GetMapping("interval")
    public ResponseEntity<?> getTransactionsInterval(BTQuery query) {
        try {
            return new ResponseEntity<>(service.getTransactionsInterval(query), HttpStatus.OK);
        } catch (ParseException e) {
            return new ResponseEntity<>("Ошибка", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("books")
    public ResponseEntity<?> getBooksTransactionsCount(BTQuery query) {
        try {
            return new ResponseEntity<>(service.getBooksTransactionsCount(query), HttpStatus.OK);
        } catch (ParseException e) {
            return new ResponseEntity<>("Ошибка", HttpStatus.CONFLICT);
        }
    }

}
