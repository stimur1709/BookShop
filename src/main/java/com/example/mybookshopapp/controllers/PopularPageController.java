package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
@Tag(name = "Страница популярное", description = "Выводит на странице книги в порядке убывания популярности")
public class PopularPageController {

    private final BookService bookService;

    @Autowired
    public PopularPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/api/books/popular")
    @ResponseBody
    @Operation(summary = "Постраничный вывод книг")
    public BooksPageDto getPopularBooksPage(@RequestParam("offset") Integer offset,
                                            @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit).getContent());
    }

    @GetMapping("/books/popular")
    public String popularPage(Model model) {
        model.addAttribute("popularBooks", bookService.getPageOfPopularBooks(0, 20).getContent());
        model.addAttribute("searchWordDto", new SearchWordDto());
        model.addAttribute("searchResult", new ArrayList<>());
        return "books/popular";
    }
}