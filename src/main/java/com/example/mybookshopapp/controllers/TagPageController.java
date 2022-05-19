package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksTagPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TagPageController {

    private final TagService tagService;

    @Autowired
    public TagPageController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags/{slug}")
    public String tagPage(@PathVariable(value = "slug", required = false) String slug, Model model) {
        model.addAttribute("tagPage", tagService.getPageBySlug(slug, 0, 20));
        return "tags/index";
    }

    @GetMapping("/api/books/tag/{slug}")
    @ResponseBody
    public BooksTagPageDto getPopularBooksPage(@PathVariable(value = "slug") String slug,
                                            @RequestParam("offset") Integer offset,
                                            @RequestParam("limit") Integer limit) {
        return new BooksTagPageDto(tagService.getPageBySlug(slug, offset, limit).getContent());
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