package com.example.mybookshopapp.data.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
@Setter
public class TransactionsInterval {

    private Date intervalTime;
    private int transactionCount;
}
