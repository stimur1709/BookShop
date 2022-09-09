package com.example.mybookshopapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BooksStatusRequestDto {
    private String booksIds;
    private Status status;

    @Override
    public String toString() {
        return "BooksStatusRequestDto{" +
                "booksIds='" + booksIds + '\'' +
                ", status=" + status +
                '}';
    }
}
