package com.example.mybookshopapp.data.dto;

import com.example.mybookshopapp.data.entity.Genre;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GenreDto extends Dto {

    private Genre parent;

    private String slug;

    private String name;

    private List<Genre> childGenres;

    private List<BooksFDto> bookList;
}
