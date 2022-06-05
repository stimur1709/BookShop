package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.ResourceStorage;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.service.AuthorService;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@Controller
public class BookPageController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final TagService tagService;
    private final ResourceStorage storage;

    @Autowired
    public BookPageController(BookService bookService, AuthorService authorService,
                              TagService tagService, ResourceStorage storage) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.tagService = tagService;
        this.storage = storage;
    }

    @GetMapping("/books/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model) {
        BookEntity book = bookService.getBookBySlug(slug);
        model.addAttribute("slugBook", book);
        model.addAttribute("authorsBook", authorService.getAuthorsByBook(book.getId()));
        model.addAttribute("tagsBook", tagService.getTagsByBook(book.getId()));
        model.addAttribute("searchWordDto", new SearchWordDto());
        model.addAttribute("searchResult", new ArrayList<>());
        return "books/slug";
    }

    @PostMapping("/books/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file, @PathVariable("slug") String slug) throws IOException {
        String savePath = storage.saveNewBookImage(file, slug);
        BookEntity bookToUpdate = bookService.getBookBySlug(slug);
        bookToUpdate.setImage(savePath);
        bookService.save(bookToUpdate);
        return "redirect:/books/" + slug;
    }
}
