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
@Tag(name = "Страница новинок", description = "Выводит на странице книги в порядке убывания даты публикации")
public class RecentPageController extends ModelAttributeController {

    private final BookService bookService;

    @Autowired
    public RecentPageController(BookService bookService, UserProfileService userProfileService,
                                BookShopService bookShopService, MessageSource messageSource, LocaleResolver localeResolver) {
        super(userProfileService, bookShopService, messageSource, localeResolver);
        this.bookService = bookService;
    }

    @GetMapping("/api/books/recent")
    @ResponseBody
    @Operation(summary = "Постраничный вывод книг с указанием параметров даты \"от\" и \"до\"")
    public BooksPageDto getRecentBooksPage(@RequestParam(value = "from", defaultValue = "16.06.2009") String from,
                                           @RequestParam(value = "to", defaultValue = "01.01.2035") String to,
                                           @RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfPubDateBetweenBooks(from, to, offset, limit).getContent());
    }

    @GetMapping("/books/recent")
    public String recentPage(Model model) {
        Page<Book> books = bookService.getPageBooks(0, 20, "pubDate");
        model.addAttribute("recentBooks", books.getContent());
        model.addAttribute("show", books.getTotalPages() > 1);
        model.addAttribute("totalPages", books.getTotalPages());

        return "books/recent";
    }
}