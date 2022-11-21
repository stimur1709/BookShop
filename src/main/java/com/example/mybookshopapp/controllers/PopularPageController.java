package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

@Controller
@Tag(name = "Страница популярное", description = "Выводит на странице книги в порядке убывания популярности")
public class PopularPageController extends ModelAttributeController {

    private final BookService bookService;

    @Autowired
    public PopularPageController(BookService bookService, UserProfileService userProfileService, BookShopService bookShopService,
                                 MessageSource messageSource, LocaleResolver localeResolver) {
        super(userProfileService, bookShopService, messageSource, localeResolver);
        this.bookService = bookService;
    }

    @GetMapping("/api/books/popular")
    @ResponseBody
    @Operation(summary = "Постраничный вывод книг")
    public BooksPageDto getPopularBooksPage(@RequestParam("offset") Integer offset,
                                            @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageBooks(offset, limit, "popularity").getContent());
    }

    @GetMapping("/books/popular")
    public String popularPage(Model model) {
        Page<Book> books = bookService.getPageBooks(0, 20, "popularity");
        model.addAttribute("books", books);
        return "books/popular";
    }
}