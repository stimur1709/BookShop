package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.entity.author.Author;
import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.entity.book.links.Book2UserEntity;
import com.example.mybookshopapp.entity.book.links.Book2UserTypeEntity;
import com.example.mybookshopapp.service.AuthorService;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.BooksRatingAndPopularityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class BooksRestApiController {

    private final BookService bookService;

    @Autowired
    public BooksRestApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books/resent1")
    public ResponseEntity<List<BookEntity>> booksRecent(@RequestParam("from") String from, @RequestParam("to") String to,
                                                        @RequestParam("offset") Integer offset,
                                                        @RequestParam("limit") Integer limit) {
        try {
            Date dateFrom = new SimpleDateFormat("dd.MM.yyyy").parse(from);
            Date dateTo = new SimpleDateFormat("dd.MM.yyyy").parse(to);
            return ResponseEntity.ok(bookService.getPageOfPubDateBetweenBooks(dateFrom, dateTo, offset, limit).getContent());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}