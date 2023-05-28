package com.example.mybookshopapp.data.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class BalanceTransactionDto extends Dto {

    private String description;
    private int value;
    private BookDto books;
    private Date time;

}