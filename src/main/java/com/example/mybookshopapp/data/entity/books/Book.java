package com.example.mybookshopapp.data.entity.books;

import com.example.mybookshopapp.data.entity.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book extends Models {

    @Column(name = "pub_date", columnDefinition = "DATE NOT NULL")
    private Date pubDate;

    @Column(name = "is_bestseller", columnDefinition = "SMALLINT NOT NULL")
    private int isBestseller;

    @Column(columnDefinition = "VARCHAR(19) NOT NULL", unique = true)
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String title;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "INT NOT NULL")
    private int price;

    @Column(columnDefinition = "double precision NOT NULL DEFAULT 0")
    private Double discount;

    @Column(columnDefinition = "double precision NOT NULL DEFAULT 0")
    private Double popularity;

    @Transient
    private int discountPrice;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "book2tag",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<TagBook> tagList;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "book2Author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<Author> authorList;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "book2genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genreList;

    public int getDiscountPrice() {
        if (discount == 0) {
            return price;
        } else {
            return price - Math.toIntExact(Math.round(price * discount));
        }
    }
}