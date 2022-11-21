package com.example.mybookshopapp.model.book;

import com.example.mybookshopapp.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "book_rating")
public class BookRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "rating", columnDefinition = "INT NOT NULL DEFAULT 0")
    private int rating;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    @JsonBackReference
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    @JsonBackReference
    private User user;

    public BookRating() {
    }

    public BookRating(int rating, Book book, User user) {
        this.rating = rating;
        this.book = book;
        this.user = user;
    }
}
