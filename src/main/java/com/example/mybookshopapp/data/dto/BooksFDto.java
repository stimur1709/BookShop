package com.example.mybookshopapp.data.dto;

import com.example.mybookshopapp.data.entity.Author;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class BooksFDto extends Dto {

    private Double discount;

    private String image;

    private short isBestseller;

    private Double popularity;

    private int price;

    private String slug;

    private String title;

    private Date pubDate;

    private String code;

    private Double rate;

    private List<Author> authorList;

    public int discountPrice() {
        if (discount == 0) {
            return price;
        } else {
            return price - Math.toIntExact(Math.round(price * discount));
        }
    }
}
