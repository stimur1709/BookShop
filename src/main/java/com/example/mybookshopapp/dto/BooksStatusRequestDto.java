package com.example.mybookshopapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BooksStatusRequestDto {
    private String bookIds;
    private Status status;

    public BooksStatusRequestDto(String bookIds, Status status) {
        this.bookIds = bookIds;
        this.status = status;
    }
}
