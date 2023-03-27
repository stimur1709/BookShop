package com.example.mybookshopapp.data.entity.news;

import com.example.mybookshopapp.data.entity.author.Author;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class BooksQueryNew extends Model {

    private Double discount;

    private String image;

    @Column(name = "is_bestseller")
    private short isBestseller;

    private Double popularity;

    private int price;

    private String slug;

    private String title;

    @Column(name = "pub_date")
    private Date pubDate;

    private String code;

    private Double rate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2Author",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    @JsonManagedReference
    private List<Author> authorList = new ArrayList<>();

}
