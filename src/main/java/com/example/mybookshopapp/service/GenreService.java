package com.example.mybookshopapp.service;

import com.example.mybookshopapp.entity.genre.GenreEntity;
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

    public Map<GenreEntity, Map<GenreEntity, List<GenreEntity>>> getGenreMap() {

        Map<GenreEntity, Map<GenreEntity, List<GenreEntity>>> genreTreeList = new HashMap<>();

        List<GenreEntity> genreList = genreRepository.findAll(Sort.by(Sort.Direction.DESC, "amount"));
        for (GenreEntity genreOne : genreList) {
            Map<GenreEntity, List<GenreEntity>> genreTwoList = new HashMap<>();
            for (GenreEntity genreTwo : genreList) {
                List<GenreEntity> genreThreeList = new ArrayList<>();
                for (GenreEntity genreThree : genreList) {
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

    public GenreEntity getPageBySlug(String slug) {
        return genreRepository.findGenreEntityBySlug(slug);
    }

    public GenreEntity getPageById(Integer id) {
        return genreRepository.findGenreEntityById(id);
    }

    public void addAmount() {
        List<GenreEntity> bookList = genreRepository.findAll();
        bookList.forEach(genre -> {
            genre.setAmount(genre.getBookList().size());
            genreRepository.save(genre);
        });
    }

}
