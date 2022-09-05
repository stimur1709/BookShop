package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.author.Author;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.repository.AuthorRepository;
import com.example.mybookshopapp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public TreeMap<String, List<Author>> getAuthorsMap() {
        return new TreeMap<>(authorRepository.findAll().stream()
                .collect(Collectors.groupingBy((Author a) -> a.getName().substring(0, 1))));
    }

    public Author getAuthorsBySlug(String slug) {
        return authorRepository.findAuthorBySlug(slug);
    }

    public Author getAuthorsById(Integer id) {
        return authorRepository.findAuthorById(id);
    }

    public List<Author> getAuthorsByBook(Integer id) {
        return bookRepository.findById(id).map(Book::getAuthorList).orElse(Collections.emptyList());
    }
}