package com.example.mybookshopapp.dto;

import com.example.mybookshopapp.model.book.links.BookCodeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookStatusRequestDto {
    private String booksIds;
    private BookCodeType status;

    public BookStatusRequestDto(String booksIds, BookCodeType status) {
        this.booksIds = booksIds;
        this.status = status;
    }


}
