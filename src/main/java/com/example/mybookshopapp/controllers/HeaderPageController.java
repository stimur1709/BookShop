package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.service.BookShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HeaderPageController {

    private final BookShopService bookShopService;

    @Autowired
    public HeaderPageController(BookShopService bookShopService) {
        this.bookShopService = bookShopService;
    }

    @GetMapping("/cartSize")
    public int getCartSize(@CookieValue(name = "cartContent", required = false) String cartContents) {
        List<Book> books = bookShopService.getBooksFromCookie(cartContents);
        if (books.isEmpty())
            return 0;
        return books.size();
    }

    @GetMapping("/keptSize")
    public int getKeptSize(@CookieValue(name = "keptContent", required = false) String keptContents) {
        List<Book> books = bookShopService.getBooksFromCookie(keptContents);
        if (books.isEmpty())
            return 0;
        return books.size();
    }
}