package com.example.mybookshopapp.data.entity.book.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity
@Schema(description = "Отзывы о книгах")
public class BookReviewQuery {

    @Id
    private int id;

    private Date date;

    private String text;

    private String name;

    private Short value;

    private long likes;

    private long dislikes;

}
