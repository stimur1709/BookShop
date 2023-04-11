package com.example.mybookshopapp.data.dto.genre;

import com.example.mybookshopapp.data.dto.book.BooksFDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GenreDtoForBook extends GenreDto {

    private GenreDtoForBook parent;
    private List<GenreDtoForBook> childGenres;
    private List<BooksFDto> bookList;
}
