package com.example.mybookshopapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookReviewRequestDto {
    private int bookId;
    private String text;

    public BookReviewRequestDto(int bookId, String text) {
        this.bookId = bookId;
        this.text = text;
    }
}
