package com.example.mybookshopapp.data.dto.genre;

import com.example.mybookshopapp.data.dto.Dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreDto extends Dto {

    private String slug;
    private String name;

}
