package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.entity.author.Author;
import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.service.AuthorService;
import com.example.mybookshopapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AuthorsPageController {

    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public AuthorsPageController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping("/authors")
    public String authorsPage() {
        return "authors/index";
    }

    @GetMapping("/authors/{slug}")
    public String authorPage(@PathVariable("slug") String slug, Model model) {
        Author author = authorService.getAuthorsBySlug(slug);
        model.addAttribute("authorSlug", author);
        model.addAttribute("authorBooks", bookService.getBooksForPageAuthor(author, 0, 5).getContent());
        return "authors/slug";
    }

    @GetMapping("/books/author/{slug}")
    public String authorBooksPage(@PathVariable("slug") String slug, Model model) {
        Author author = authorService.getAuthorsBySlug(slug);
        model.addAttribute("authorSlug", author);
        model.addAttribute("authorBooks", bookService.getBooksForPageAuthor(author, 0, 20).getContent());
        return "books/author";
    }

    @GetMapping("/api/books/author/{id}")
    @ResponseBody
    public BooksPageDto authorBooksPage(@PathVariable("id") Integer id, @RequestParam("offset") Integer offset,
                                        @RequestParam("limit") Integer limit) {
        Author author = authorService.getAuthorsById(id);
        return new BooksPageDto(bookService.getBooksForPageAuthor(author, offset, limit).getContent());
    }

    @ModelAttribute("authorsMap")
    public Map<String, List<Author>> authorsMap() {
        return authorService.getAuthorsMap();
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
