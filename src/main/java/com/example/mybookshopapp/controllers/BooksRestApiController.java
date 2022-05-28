package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BooksRestApiController {

    private final BookService bookService;
    private final TagService tagService;

    @Autowired
    public BooksRestApiController(BookService bookService, TagService tagService) {
        this.bookService = bookService;
        this.tagService = tagService;
    }
}