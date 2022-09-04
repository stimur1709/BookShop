package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.UserProfileService;
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
@Tag(name = "Страница новинок", description = "Выводит на странице книги в порядке убывания даты публикации")
public class RecentPageController extends ModelAttributeController {

    private final BookService bookService;

    @Autowired
    public RecentPageController(BookService bookService, UserProfileService userProfileService) {
        super(userProfileService);
        this.bookService = bookService;
    }

    @GetMapping("/api/books/recent")
    @ResponseBody
    @Operation(summary = "Постраничный вывод книг с указанием параметров даты \"от\" и \"до\"")
    public BooksPageDto getRecentBooksPage(@RequestParam(value = "from", defaultValue = "16.06.2009") String from,
                                           @RequestParam(value = "to", defaultValue = "01.01.2035") String to,
                                           @RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit, Model model) {
        model.addAttribute("offset", bookService.getPageOfRecentBooks(offset, limit).getNumber());
        model.addAttribute("limit", bookService.getPageOfRecentBooks(offset, limit).getTotalPages());
        return new BooksPageDto(bookService.getPageOfPubDateBetweenBooks(from, to, offset, limit).getContent());
    }

    @GetMapping("/books/recent")
    public String recentPage(Model model) {
        model.addAttribute("recentBooks", bookService.getPageOfRecentBooks(0, 20).getContent());
        model.addAttribute("searchWordDto", new SearchWordDto());
        model.addAttribute("searchResult", new ArrayList<>());
        model.addAttribute("offset", bookService.getPageOfRecentBooks(0, 20).getNumber());
        model.addAttribute("limit", bookService.getPageOfRecentBooks(0, 100).getTotalPages());
        return "books/recent";
    }
}