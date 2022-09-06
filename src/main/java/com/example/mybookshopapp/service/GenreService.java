package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.genre.Genre;
import com.example.mybookshopapp.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final GenreRepository genreRepository;


    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Map<Genre, Map<Genre, List<Genre>>> getGenreMap() {

        Map<Genre, Map<Genre, List<Genre>>> genreTreeList = new TreeMap<>();

        List<Genre> genreList = genreRepository.findAll();
        for (Genre genreOne : genreList) {
            Map<Genre, List<Genre>> genreTwoList = new HashMap<>();
            for (Genre genreTwo : genreList) {
                List<Genre> genreThreeList = new ArrayList<>();
                for (Genre genreThree : genreList) {
                    if (genreThree.getParentId() == genreTwo.getId()) {
                        genreThreeList.add(genreThree);
                    }
                }
                if (genreTwo.getParentId() == genreOne.getId()) {
                    genreTwoList.put(genreTwo, genreThreeList.stream()
                            .sorted(Comparator.comparing(Genre::getAmount, Comparator.reverseOrder()))
                            .collect(Collectors.toList()));
                }
            }
            if (genreOne.getParentId() == 0) {
                genreTreeList.put(genreOne, genreTwoList);
            }
        }
        return genreTreeList;
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
