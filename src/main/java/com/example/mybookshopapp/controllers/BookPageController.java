package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.service.AuthorService;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class BookPageController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final TagService tagService;

    @Autowired
    public BookPageController(BookService bookService, AuthorService authorService, TagService tagService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.tagService = tagService;
    }

    @GetMapping("/books/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model) {
        BookEntity book = bookService.getBookBySlug(slug);
        model.addAttribute("slugBook", book);
        model.addAttribute("authorsBook", authorService.getAuthorsByBook(book.getId()));
        model.addAttribute("tagsBook", tagService.getTagsByBook(book.getId()));
        model.addAttribute("searchWordDto", new SearchWordDto());
        model.addAttribute("searchResult", new ArrayList<>());
        return "books/slug";
    }
}
