package com.example.mybookshopapp.data.outher;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRateRequestDto {

    private int bookId;
    private int value;

    public BookRateRequestDto(int bookId, int value) {
        this.bookId = bookId;
        this.value = value;
    }
}
