package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dao.BooksTransactionsCount;
import com.example.mybookshopapp.data.dao.TransactionsInterval;
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
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class RestBalanceTransactionController
        extends RestDataControllerImpl<BTQuery, BalanceTransactionDto, BalanceTransactionDto, BalanceTransactionService> {

    @Autowired
    public RestBalanceTransactionController(BalanceTransactionService service) {
        super(service);
    }

    @GetMapping("interval")
    public ResponseEntity<List<TransactionsInterval>> getTransactionsInterval(BTQuery query) throws ParseException {
        return new ResponseEntity<>(service.getTransactionsInterval(query), HttpStatus.OK);
    }

    @GetMapping("books")
    public ResponseEntity<List<BooksTransactionsCount>> getBooksTransactionsCount(BTQuery query) throws ParseException {
        return new ResponseEntity<>(service.getBooksTransactionsCount(query), HttpStatus.OK);
    }

}
