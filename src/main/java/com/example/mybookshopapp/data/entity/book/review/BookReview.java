package com.example.mybookshopapp.data.entity.book.review;

import com.example.mybookshopapp.data.entity.book.Book;
import com.example.mybookshopapp.data.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "book_id")
    @JsonBackReference
    private Book book;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "user_id")
    @JsonBackReference
    private User user;

    @Column(columnDefinition = "DATE NOT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

    public BookReview(Book book, User user, String text) {
        this.book = book;
        this.user = user;
        this.time = new Date();
        this.text = text;
    }

    public BookReview() {
    }

}
