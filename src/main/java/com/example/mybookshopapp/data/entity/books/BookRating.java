package com.example.mybookshopapp.data.entity.books;

import com.example.mybookshopapp.data.entity.links.key.KeyBook2User;
import com.example.mybookshopapp.data.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "book_rating")
public class BookRating {

    @EmbeddedId
    private KeyBook2User keyBook2User;

    @Column(name = "rating", columnDefinition = "INT NOT NULL DEFAULT 0")
    private int rating;

    @ManyToOne
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    @JsonBackReference
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonBackReference
    private User user;

    public BookRating() {
    }

}
