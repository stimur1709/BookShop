package com.example.mybookshopapp.model.book.review;

import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @OneToMany(mappedBy = "bookReview")
    @JsonBackReference
    private List<BookReviewLike> reviewLikeList = new ArrayList<>();

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int rate;

    @Transient
    private short value;

    public BookReview(Book book, User user, String text) {
        this.book = book;
        this.user = user;
        this.time = new Date();
        this.text = text;
    }

    public BookReview() {
    }

    public long getLikes() {
        return getReviewLikeList().stream()
                .filter(bookReviewLikeEntity -> bookReviewLikeEntity.getValue() == 1).count();
    }

    public long getDislikes() {
        return getReviewLikeList().stream()
                .filter(bookReviewLikeEntity -> bookReviewLikeEntity.getValue() == -1).count();
    }
}
