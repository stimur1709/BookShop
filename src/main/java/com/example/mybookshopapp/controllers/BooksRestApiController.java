package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class BooksRestApiController {

    private final BookService bookService;
    private final TagService tagService;

    @Autowired
    public BooksRestApiController(BookService bookService, TagService tagService) {
        this.bookService = bookService;
        this.tagService = tagService;
    }


    @GetMapping("/api/books/recent1")
    @ResponseBody
    public BooksPageDto getRecentBooksPage(@RequestParam("from") String from, @RequestParam("to") String to,
                                           @RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit) {
        try {
            Date dateFrom = new SimpleDateFormat("dd.MM.yyyy").parse(from);
            Date dateTo = new SimpleDateFormat("dd.MM.yyyy").parse(to);
            return new BooksPageDto(bookService.getPageOfPubDateBetweenBooks(dateFrom, dateTo,
                    offset, limit).getContent());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}