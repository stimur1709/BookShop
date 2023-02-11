package com.example.mybookshopapp.data.entity.payments;

import com.example.mybookshopapp.data.entity.book.Book;
import lombok.Data;

import java.io.Serializable;

@Data
public class BalanceTransactionDto implements Serializable {

    private String description;
    private int value;
    private Book books;
    private String formatDate;

    public BalanceTransactionDto() {
    }
}