package com.example.mybookshopapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BooksStatusRequestDto {
    private String booksIds;
    private Status status;

    public BooksStatusRequestDto(String booksIds, Status status) {
        this.booksIds = booksIds;
        this.status = status;
    }
}
