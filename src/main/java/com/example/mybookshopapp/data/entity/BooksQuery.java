package com.example.mybookshopapp.data.entity;

import com.example.mybookshopapp.data.entity.author.Author;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Schema(description = "Сущность для получения списка книг")
@Entity
public class BooksQuery {

    @Id
    @Schema(example = "1")
    private int id;

    @Schema(example = "0.1")
    private Double discount;

    @Schema(example = "Картинка книги")
    private String image;

    @Column(name = "is_bestseller")
    @Schema(example = "1")
    private short isBestseller;

    @Schema(example = "0.4")
    private Double popularity;

    @Schema(example = "1000")
    private int price;

    @Schema(example = "kdrmsjtkjippesbmrvy")
    private String slug;

    @Schema(example = "Анна Каренина")
    private String title;

    @Column(name = "pub_date")
    @Schema(example = "2010-02-02")
    private Date pubDate;

    private String code;

    private Double rate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2Author",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    @JsonManagedReference
    private List<Author> authorList = new ArrayList<>();

    public int discountPrice() {
        if (discount == 0) {
            return price;
        } else {
            return price - Math.toIntExact(Math.round(price * discount));
        }
    }

}
