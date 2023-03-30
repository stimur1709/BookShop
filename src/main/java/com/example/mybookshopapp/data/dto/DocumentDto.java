package com.example.mybookshopapp.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentDto extends Dto {

    private int sortIndex;
    private String slug;
    private String title;
    private String htmlText;
    private String description;
    private String image;

}
