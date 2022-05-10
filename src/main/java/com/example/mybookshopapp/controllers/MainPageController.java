package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.book.BookEntity;
import com.example.mybookshopapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bookshop")
public class MainPageController {

    private final BookService bookService;

    @Autowired
    public MainPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/main")
    public String mainPage(Model model) {
        model.addAttribute("bookData", bookService.getBooksData());
        return "index";
    }

    @GetMapping("/fragment")
    public String fragmentPage(Model model, BookEntity bookEntity) {
        model.addAttribute("bookData", bookService.getBooksData());
        return "fragments/books_pool_fragment";
    }
}

