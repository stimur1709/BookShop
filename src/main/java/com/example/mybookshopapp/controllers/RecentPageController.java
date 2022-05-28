package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RecentPageController {

    private final BookService bookService;

    @Autowired
    public RecentPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/api/books/recent")
    @ResponseBody
    public BooksPageDto getRecentBooksPage(@RequestParam("from") String from, @RequestParam("to") String to,
                                           @RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit) {
        if (from.equals("")) {
            return new BooksPageDto(bookService.getPageOfPubDateBeforeBooks(to,
                    offset, limit).getContent());
        }
        if (to.equals("")) {
            return new BooksPageDto(bookService.getPageOfPubDateAfterBooks(from,
                    offset, limit).getContent());
        } else {
            return new BooksPageDto(bookService.getPageOfPubDateBetweenBooks(from, to,
                    offset, limit).getContent());
        }
    }

    @GetMapping("/books/recent")
    public String recentPage(Model model) {
        model.addAttribute("recentBooks", bookService.getPageOfRecentBooks(0, 20).getContent());
        return "books/recent";
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("searchResult")
    public List<BookEntity> searchResult() {
        return new ArrayList<>();
    }
}