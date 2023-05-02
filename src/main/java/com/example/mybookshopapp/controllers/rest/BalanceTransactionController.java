package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.BalanceTransactionDto;
import com.example.mybookshopapp.data.query.BTQuery;
import com.example.mybookshopapp.service.BalanceTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class BalanceTransactionController
        extends RestDataControllerImpl<BTQuery, BalanceTransactionDto, BalanceTransactionDto, BalanceTransactionService> {

    @Autowired
    public BalanceTransactionController(BalanceTransactionService service) {
        super(service);
    }
}
