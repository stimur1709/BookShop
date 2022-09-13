package com.example.mybookshopapp.dto;

import com.example.mybookshopapp.model.book.links.BookCodeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BooksStatusRequestDto {
    private String booksIds;
    private BookCodeType status;

    public BooksStatusRequestDto(String booksIds, BookCodeType status) {
        this.booksIds = booksIds;
        this.status = status;
    }
}
