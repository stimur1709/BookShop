package com.example.mybookshopapp.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BookReviewRequestDto {
    private int bookId;

    @NotBlank(message = "{message.reviewEmpty}")
    private String text;

    public BookReviewRequestDto(int bookId, String text) {
        this.bookId = bookId;
        this.text = text;
    }
}
