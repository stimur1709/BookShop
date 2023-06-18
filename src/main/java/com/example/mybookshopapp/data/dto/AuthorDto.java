package com.example.mybookshopapp.data.dto;

import com.example.mybookshopapp.data.dto.book.BooksFDto;
import com.example.mybookshopapp.data.entity.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthorDto extends Dto {

    private String slug;
    private String name;
    private String description;
    private Image image;
    private List<BooksFDto> bookList;

}