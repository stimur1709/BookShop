package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.book.BookEntity;
import com.example.mybookshopapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksPageController {

    private final BookService bookService;

    @Autowired
    public BooksPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/recent")
    public String recentPage() {
        return "books/recent";
    }

    @GetMapping("/popular")
    public String popularPage() {
        return "books/popular";
    }

    @GetMapping("/fragment")
    public String fragmentPage(Model model) {
        model.addAttribute("bookList", bookService.getBooksData());
        return "fragments/books_pool_fragment";
    }

    @ModelAttribute("bookList")
    public List<BookEntity> bookList() {
        return bookService.getBooksData();
    }
}
