package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dto.GenreDto;
import com.example.mybookshopapp.model.genre.Genre;
import com.example.mybookshopapp.repository.GenreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public GenreService(GenreRepository genreRepository, ModelMapper modelMapper) {
        this.genreRepository = genreRepository;
        this.modelMapper = modelMapper;
    }

    public Map<GenreDto, Map<GenreDto, List<GenreDto>>> getGenreMap() {

        Map<GenreDto, Map<GenreDto, List<GenreDto>>> genreTreeList = new TreeMap<>();

        List<Genre> genreList = genreRepository.findAll();
        for (Genre genreOne : genreList) {
            Map<GenreDto, List<GenreDto>> genreTwoList = new HashMap<>();
            for (Genre genreTwo : genreList) {
                List<GenreDto> genreThreeList = new ArrayList<>();
                for (Genre genreThree : genreList) {
                    if (genreThree.getParentId() == genreTwo.getId()) {
                        genreThreeList.add(convertToGenreDto(genreThree));
                    }
                }
                if (genreTwo.getParentId() == genreOne.getId()) {
                    genreTwoList.put(convertToGenreDto(genreTwo), genreThreeList.stream()
                            .sorted(Comparator.comparing(GenreDto::getAmount, Comparator.reverseOrder()))
                            .collect(Collectors.toList()));
                }
            }
            if (genreOne.getParentId() == 0) {
                genreTreeList.put(convertToGenreDto(genreOne), genreTwoList);
            }
        }
        return genreTreeList;
    }

    public GenreDto getPageBySlug(String slug) {
        return convertToGenreDto(genreRepository.findGenreEntityBySlug(slug));
    }

    public GenreDto getPageById(String id) {
        return convertToGenreDto(genreRepository.findGenreEntityBySlug(id));
    }

    public void addAmount() {
        List<Genre> bookList = genreRepository.findAll();
        bookList.forEach(genre -> {
            genre.setAmount(genre.getBookList().size());
            genreRepository.save(genre);
        });
    }

    private GenreDto convertToGenreDto(Genre genre) {
        return modelMapper.map(genre, GenreDto.class);
    }
}
