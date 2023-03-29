package com.example.mybookshopapp.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthorDto extends Dto {

    private String photo;
    private String slug;
    private String name;
    private String description;
    private List<BooksFDto> bookList;

}