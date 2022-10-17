package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Tag(name = "Страница популярное", description = "Выводит на странице книги в порядке убывания популярности")
public class PopularPageController extends ModelAttributeController {

    private final BookService bookService;

    @Autowired
    public PopularPageController(BookService bookService, UserProfileService userProfileService, BookShopService bookShopService) {
        super(userProfileService, bookShopService);
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
        Page<Book> books = bookService.getPageOfPopularBooks(0, 20);
        model.addAttribute("popularBooks", books.getContent());
        model.addAttribute("show", books.getTotalPages() > 1);
        model.addAttribute("totalPages", books.getTotalPages());
        return "books/popular";
    }
}