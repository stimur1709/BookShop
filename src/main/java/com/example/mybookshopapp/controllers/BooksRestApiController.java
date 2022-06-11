package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.entity.author.Author;
import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.service.AuthorService;
import com.example.mybookshopapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BooksRestApiController {

    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public BooksRestApiController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping("/api/books1/author/{id}")
    public ResponseEntity<List<BookEntity>> booksByAuthor(@PathVariable("id") Integer id, @RequestParam("offset") Integer offset,
                                                          @RequestParam("limit") Integer limit) {
        Author author = authorService.getAuthorsById(id);
        return ResponseEntity.ok(bookService.getBooksForPageAuthor(author, offset, limit).getContent());
    }
}
