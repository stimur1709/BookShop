package com.example.mybookshopapp.data.dto;

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
    private List<BooksFDto> bookList;
    private Image image;

}