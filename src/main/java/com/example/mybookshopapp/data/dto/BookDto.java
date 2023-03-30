package com.example.mybookshopapp.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BookDto {
    private Date pubDate;
    private int isBestseller;
    private String slug;
    private String title;
    private String image;
    private String description;
    private int price;
    private Double discount;
    private Double popularity;
    private int discountPrice;
}
