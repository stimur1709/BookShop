package com.example.mybookshopapp.dto;

import com.example.mybookshopapp.model.book.Book;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BooksPageDto {

    private Integer count;
    private List<Book> books;

    public BooksPageDto(List<Book> books) {
        this.books = books;
        this.count = books.size();
    }

    public BooksPageDto(List<Book> books, Integer count) {
        this.count = count;
        this.books = books;
    }
}
