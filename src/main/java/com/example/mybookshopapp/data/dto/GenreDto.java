package com.example.mybookshopapp.data.dto;

import com.example.mybookshopapp.data.dto.book.BooksFDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GenreDto extends Dto {

    private String slug;
    private String name;

    @JsonIgnoreProperties(value = "bookList")
    private GenreDto parent;

    @JsonIgnoreProperties(value = {"bookList", "parent"})
    private List<GenreDto> childGenres;

    private List<BooksFDto> bookList;

}
