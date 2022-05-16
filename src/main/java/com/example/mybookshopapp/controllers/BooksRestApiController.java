package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.book.BookEntity;
import com.example.mybookshopapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BooksRestApiController {

    private final BookService bookService;

    @Autowired
    public BooksRestApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books/resent1")
    public ResponseEntity<List<BookEntity>> booksRecent() {
        return ResponseEntity.ok(bookService.getPageOfRecentBooks(1,10).getContent());
    }
}