package com.example.mybookshopapp.data.dto.book;

import com.example.mybookshopapp.data.dto.AuthorDto;
import com.example.mybookshopapp.data.dto.Dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class BooksFDto extends Dto {

    @Size(min = 2, max = 255, message = "{message.titleEmpty}")
    @NotNull(message = "{message.titleEmpty}")
    private String title;

    @Min(value = 0, message = "{message.validateDiscount}")
    @Max(value = 1, message = "{message.validateDiscount}")
    private Double discount;

    @Min(value = 0, message = "{message.validateBestseller}")
    @Max(value = 1, message = "{message.validateBestseller}")
    private short isBestseller;

    @Min(value = 10, message = "{message.validatePrice}")
    private int price;

    private String image;
    private Integer imageId;
    private Double popularity;
    private String slug;
    private Date pubDate;
    private String code;
    private Double rate;

    @JsonIgnoreProperties({"bookList", "image"})
    private List<AuthorDto> authorList;

    public int discountPrice() {
        if (discount == 0) {
            return price;
        } else {
            return price - Math.toIntExact(Math.round(price * discount));
        }
    }
}
