package com.example.mybookshopapp.data.entity.books;

import com.example.mybookshopapp.data.entity.Models;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.Date;

@Getter
@Setter
@Entity
public class BookReviewQuery extends Models {

    private Date date;

    private String text;

    private String name;

    private Short value;

    private long likes;

    private long dislikes;

}
