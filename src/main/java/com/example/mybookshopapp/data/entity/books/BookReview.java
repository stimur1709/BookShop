package com.example.mybookshopapp.data.entity.books;

import com.example.mybookshopapp.data.entity.Models;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "book_review")
public class BookReview extends Models {

    @Column(columnDefinition = "INT NOT NULL", name = "book_id")
    private int bookId;

    @Column(columnDefinition = "INT NOT NULL", name = "user_id")
    private int userId;

    @Column(columnDefinition = "DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

    private short status;

    public BookReview(int bookId, int userId, String text) {
        this.bookId = bookId;
        this.userId = userId;
        this.time = new Date();
        this.text = text;
    }

    public BookReview() {
    }

}
