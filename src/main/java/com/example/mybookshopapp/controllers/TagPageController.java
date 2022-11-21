package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.tag.TagBook;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.TagService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import java.util.ArrayList;

@Controller
@io.swagger.v3.oas.annotations.tags.Tag(name = "Страница тэга", description = "Выводит на странице список книг, привязанных к данному тэгу")
public class TagPageController extends ModelAttributeController {

    private final TagService tagService;
    private final BookService bookService;

    @Autowired
    public TagPageController(TagService tagService, BookService bookService,
                             UserProfileService userProfileService, BookShopService bookShopService,
                             MessageSource messageSource, LocaleResolver localeResolver) {
        super(userProfileService, bookShopService, messageSource, localeResolver);
        this.tagService = tagService;
        this.bookService = bookService;
    }

    @GetMapping("/tags/{slug}")
    public String tagPage(@PathVariable(value = "slug", required = false) String slug, Model model) {
        TagBook tag = tagService.getPageBySlug(slug);
        Page<Book> books = bookService.getBooksForPageTage(tag, 0, 20);
        model.addAttribute("tagPage", tag);
        model.addAttribute("booksTag", books);
        model.addAttribute("searchWordDto", new SearchWordDto());
        model.addAttribute("searchResult", new ArrayList<>());
        model.addAttribute("show", books.getTotalPages() > 1);
        model.addAttribute("totalPages", books.getTotalPages());
        return "tags/index";
    }

    @GetMapping("/api/books/tag/{slug}")
    @ResponseBody
    @Operation(summary = "Постраничный вывод книг")
    public BooksPageDto getBooksPage(@PathVariable(value = "slug") String slug,
                                     @RequestParam("offset") Integer offset,
                                     @RequestParam("limit") Integer limit) {
        TagBook tag = tagService.getPageBySlug(slug);
        Page<Book> tagBooks = bookService.getBooksForPageTage(tag, offset, limit);
        return new BooksPageDto(tagBooks.getContent());
    }
}