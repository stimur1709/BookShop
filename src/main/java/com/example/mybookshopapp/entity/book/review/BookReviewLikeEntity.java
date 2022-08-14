package com.example.mybookshopapp.entity.book.review;

import com.example.mybookshopapp.entity.user.UserEntity;
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
public class BookReviewLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "review_id")
    @JsonManagedReference
    private BookReviewEntity bookReview;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "user_id")
    @JsonBackReference
    private UserEntity user;

    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "SMALLINT NOT NULL")
    private short value;

    public BookReviewLikeEntity(BookReviewEntity bookReview, UserEntity user, short value) {
        this.bookReview = bookReview;
        this.user = user;
        this.time = LocalDateTime.now();
        this.value = value;
    }

    public BookReviewLikeEntity() {
    }
}
