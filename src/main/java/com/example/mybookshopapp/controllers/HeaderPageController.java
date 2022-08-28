package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HeaderPageController {

    private final BookService bookService;

    @Autowired
    public HeaderPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/api/cartSize")
    public int getCartSize(@CookieValue(value = "cartContents", required = false) String cartContents) {
        List<Book> books = bookService.getBooksFromCookie(cartContents);
        if (books.isEmpty())
            return 0;
        return books.size();
    }

    @GetMapping("/api/keptSize")
    public int getKeptSize(@CookieValue(value = "keptContents", required = false) String keptContents) {
        List<Book> books = bookService.getBooksFromCookie(keptContents);
        if (books.isEmpty())
            return 0;
        return books.size();
    }
}
