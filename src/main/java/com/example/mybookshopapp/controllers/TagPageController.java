package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.entity.tag.TagEntity;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@Tag(name = "Страница тэга", description = "Выводит на странице список книг, привязанных к данному тэгу")
public class TagPageController {

    private final TagService tagService;
    private final BookService bookService;

    @Autowired
    public TagPageController(TagService tagService, BookService bookService) {
        this.tagService = tagService;
        this.bookService = bookService;
    }

    @GetMapping("/tags/{slug}")
    public String tagPage(@PathVariable(value = "slug", required = false) String slug, Model model) {
        TagEntity tag = tagService.getPageBySlug(slug);
        model.addAttribute("tagPage" , tag);
        model.addAttribute("booksTag", bookService.getBooksForPageTage(tag, 0, 20));
        model.addAttribute("searchWordDto", new SearchWordDto());
        model.addAttribute("searchResult", new ArrayList<>());
        return "tags/index";
    }

    @GetMapping("/api/books/tag/{slug}")
    @ResponseBody
    @Operation(summary = "Постраничный вывод книг")
    public BooksPageDto getBooksPage(@PathVariable(value = "slug") String slug,
                                            @RequestParam("offset") Integer offset,
                                            @RequestParam("limit") Integer limit) {
        TagEntity tag = tagService.getPageBySlug(slug);
        return new BooksPageDto(bookService.getBooksForPageTage(tag, offset, limit).getContent());
    }
}