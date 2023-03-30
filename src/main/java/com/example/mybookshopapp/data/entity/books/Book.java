package com.example.mybookshopapp.data.entity.books;

import com.example.mybookshopapp.data.entity.Models;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

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

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String image;

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

    public int getDiscountPrice() {
        if (discount == 0) {
            return price;
        } else {
            return price - Math.toIntExact(Math.round(price * discount));
        }
    }
}