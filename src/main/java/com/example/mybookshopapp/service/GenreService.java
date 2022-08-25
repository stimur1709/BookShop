package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.genre.Genre;
import com.example.mybookshopapp.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GenreService {

    GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Map<Genre, Map<Genre, List<Genre>>> getGenreMap() {

        Map<Genre, Map<Genre, List<Genre>>> genreTreeList = new HashMap<>();

        List<Genre> genreList = genreRepository.findAll(Sort.by(Sort.Direction.DESC, "amount"));
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
                    genreTwoList.put(genreTwo, genreThreeList);
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

    public Genre getPageById(Integer id) {
        return genreRepository.findGenreEntityById(id);
    }

    public void addAmount() {
        List<Genre> bookList = genreRepository.findAll();
        bookList.forEach(genre -> {
            genre.setAmount(genre.getBookList().size());
            genreRepository.save(genre);
        });
    }

}
