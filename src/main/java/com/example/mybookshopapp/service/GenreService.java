package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.genre.Genre;
import com.example.mybookshopapp.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return genreRepository.findAll();
    }

    public Genre getPageBySlug(String slug) {
        return genreRepository.findGenreEntityBySlug(slug);
    }

    public Genre getPageById(String id) {
        return genreRepository.findGenreEntityBySlug(id);
    }
}
