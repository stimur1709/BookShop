package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.author.Author;
import com.example.mybookshopapp.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
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

}