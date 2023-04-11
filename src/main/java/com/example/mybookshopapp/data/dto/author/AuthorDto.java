package com.example.mybookshopapp.data.dto.author;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.entity.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorDto extends Dto {

    private String slug;
    private String name;
    private String description;
    private Image image;

}