package com.example.mybookshopapp.data.dto;

import com.example.mybookshopapp.data.entity.BookQuery;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BooksPageDto {

    private Integer count;
    private List<BookQuery> books;

    public BooksPageDto(List<BookQuery> books) {
        this.books = books;
        this.count = books.size();
    }

    public BooksPageDto(List<BookQuery> books, Integer count) {
        this.count = count;
        this.books = books;
    }
}
