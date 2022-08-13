package com.example.mybookshopapp.entity.book.review;

import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.entity.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "book_review")
@Schema(description = "Отзывы о книгах")
public class BookReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "book_id")
    @JsonBackReference
    private BookEntity book;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "user_id")
    @JsonBackReference
    private UserEntity user;

    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

    @OneToMany(mappedBy = "bookReview")
    @JsonBackReference
    private List<BookReviewLikeEntity> reviewLikeList = new ArrayList<>();

    public BookReviewEntity(BookEntity book, UserEntity user, String text) {
        this.book = book;
        this.user = user;
        this.time = LocalDateTime.now();
        this.text = text;
    }

    public BookReviewEntity() {
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
