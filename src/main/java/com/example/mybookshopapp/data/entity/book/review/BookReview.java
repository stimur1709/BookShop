package com.example.mybookshopapp.data.entity.book.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "book_review")
@Schema(description = "Отзывы о книгах")
public class BookReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "INT NOT NULL", name = "book_id")
    private int bookId;

    @Column(columnDefinition = "INT NOT NULL", name = "user_id")
    private int userId;

    @Column(columnDefinition = "DATE NOT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

    public BookReview(int bookId, int userId, String text) {
        this.bookId = bookId;
        this.userId = userId;
        this.time = new Date();
        this.text = text;
    }

    public BookReview() {
    }

}
