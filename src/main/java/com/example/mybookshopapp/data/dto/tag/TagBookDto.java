package com.example.mybookshopapp.data.dto.tag;

import com.example.mybookshopapp.data.dto.Dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagBookDto extends Dto {

    private String name;
    private String slug;

}
