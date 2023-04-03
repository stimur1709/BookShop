package com.example.mybookshopapp.data.entity.books;

import com.example.mybookshopapp.data.entity.links.key.KeyBook2User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "book_rating")
public class BookRating {

    @EmbeddedId
    private KeyBook2User keyBook2User;

    @Column(name = "rating", columnDefinition = "INT NOT NULL DEFAULT 0")
    private int rating;

    @Column(name = "book_id", insertable = false, updatable = false)
    private Integer bookId;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Integer userId;

    public BookRating() {
    }

}
