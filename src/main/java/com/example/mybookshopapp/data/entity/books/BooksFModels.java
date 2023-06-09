package com.example.mybookshopapp.data.entity.books;

import com.example.mybookshopapp.data.entity.Author;
import com.example.mybookshopapp.data.entity.Models;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@MappedSuperclass
public class BooksFModels extends Models {

    protected Double discount;

    protected String image;

    protected Integer imageId;

    @Column(name = "is_bestseller")
    protected short isBestseller;

    protected Double popularity;

    protected int price;

    protected String slug;

    protected String title;

    @Column(name = "pub_date")
    protected Date pubDate;

    protected String code;

    protected Double rate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2Author",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    @JsonManagedReference
    protected List<Author> authorList = new ArrayList<>();

    public int discountPrice() {
        if (discount == 0) {
            return price;
        } else {
            return price - Math.toIntExact(Math.round(price * discount));
        }
    }
}
