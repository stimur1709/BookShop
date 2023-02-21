package com.example.mybookshopapp.data.dto;

import com.example.mybookshopapp.data.entity.BooksQuery;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BooksPageDto {

    private Integer count;
    private List<BooksQuery> books;

    public BooksPageDto(List<BooksQuery> books) {
        this.books = books;
        this.count = books.size();
    }

    public BooksPageDto(List<BooksQuery> books, Integer count) {
        this.count = count;
        this.books = books;
    }
}
