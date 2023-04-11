package com.example.mybookshopapp.data.dto.book;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.dto.author.AuthorDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class BooksFDto extends Dto {

    private Double discount;
    private String image;
    private Integer imageId;
    private short isBestseller;
    private Double popularity;
    private int price;
    private String slug;
    private String title;
    private Date pubDate;
    private String code;
    private Double rate;
    private List<AuthorDto> authorList;

    public int discountPrice() {
        if (discount == 0) {
            return price;
        } else {
            return price - Math.toIntExact(Math.round(price * discount));
        }
    }
}
