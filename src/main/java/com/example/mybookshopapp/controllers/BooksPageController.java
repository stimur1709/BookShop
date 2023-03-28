package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.entity.BooksQuery;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.BooksService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;

@Controller
@Tag(name = "Страница разделов книг", description = "Выводит на странице книги в зависимости от выбранного раздела")
public class BooksPageController extends ModelAttributeController {

    private final BooksService booksService;

    @Autowired
    public BooksPageController(BooksService booksService, UserProfileService userProfileService,
                               BookShopService bookShopService, MessageSource messageSource,
                               LocaleResolver localeResolver, HttpServletRequest request) {
        super(userProfileService, bookShopService, messageSource, localeResolver, request);
        this.booksService = booksService;
    }

    @GetMapping({"/books/recent", "/books/popular", "/books/viewed"})
    public String recentPage(Model model) {
        String url = getUrl();
        Page<BooksQuery> books = booksService.getPageBooks(0, 20, getProperty(url), false);
        model.addAttribute("books", books.getContent());
        model.addAttribute("show", books.getTotalPages() > 1);
        model.addAttribute("totalPages", books.getTotalPages());
        return "books/" + url;
    }

    public String getProperty(String url) {
        String property = null;
        switch (url) {
            case "viewed":
                property = "viewed";
                break;
            case "popular":
                property = "popularity";
                break;
            case "recent":
                property = "pub_date";
                break;
        }
        return property;
    }
}