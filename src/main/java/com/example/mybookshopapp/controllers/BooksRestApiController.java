package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.entity.author.Author;
import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.entity.book.links.Book2UserEntity;
import com.example.mybookshopapp.entity.book.links.Book2UserTypeEntity;
import com.example.mybookshopapp.entity.tag.TagEntity;
import com.example.mybookshopapp.service.AuthorService;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.BooksRatingAndPopularityService;
import com.example.mybookshopapp.service.TagService;
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
    private final TagService tagService;

    @Autowired
    public BooksRestApiController(BookService bookService, TagService tagService) {
        this.bookService = bookService;
        this.tagService = tagService;
    }

    @GetMapping("/slug1")
    public ResponseEntity<TagEntity> booksRecent(@RequestParam("slug") String slug) {
        return ResponseEntity.ok(tagService.getPageBySlug(slug));

    }
}