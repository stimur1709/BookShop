package com.example.mybookshopapp.data.dto.author;

import com.example.mybookshopapp.data.dto.book.BooksFDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthorDtoForAuthor extends AuthorDto {

    private List<BooksFDto> bookList;

}