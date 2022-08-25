package com.example.mybookshopapp.model.book.review;

import com.example.mybookshopapp.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "book_review_like")
@Schema(description = "Лайки и дизлайки отзывов")
public class BookReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "review_id")
    @JsonManagedReference
    private BookReview bookReview;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "user_id")
    @JsonBackReference
    private User user;

    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "SMALLINT NOT NULL")
    private short value;

    public BookReviewLike(BookReview bookReview, User user, short value) {
        this.bookReview = bookReview;
        this.user = user;
        this.time = LocalDateTime.now();
        this.value = value;
    }

    public BookReviewLike() {
    }
}
