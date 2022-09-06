package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.genre.Genre;
import com.example.mybookshopapp.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GenreService {

    private final GenreRepository genreRepository;


    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getGenreList() {
        return genreRepository.findAll(Sort.by(Sort.Direction.DESC, "amount"));
    }

    public Genre getPageBySlug(String slug) {
        return genreRepository.findGenreEntityBySlug(slug);
    }

    public Genre getPageById(String id) {
        return genreRepository.findGenreEntityBySlug(id);
    }

    public void addAmount() {
        List<Genre> bookList = genreRepository.findAll();
        bookList.forEach(genre -> {
            genre.setAmount(genre.getBookList().size());
            genreRepository.save(genre);
        });
    }
}
