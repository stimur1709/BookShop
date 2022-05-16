package com.example.mybookshopapp.dto;

import com.example.mybookshopapp.entity.book.BookEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@Service
public class BooksPageDto {

    private Integer count;
    private List<BookEntity> books;

    public BooksPageDto(List<BookEntity> books) {
        this.books = books;
        this.count = books.size();
    }
}
