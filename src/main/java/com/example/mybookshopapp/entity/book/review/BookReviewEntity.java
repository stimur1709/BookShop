package com.example.mybookshopapp.entity.book.review;

import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.entity.user.UserEntity;
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
    private BookEntity book;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "user_id")
    private UserEntity user;

    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

    @OneToMany(mappedBy = "bookReview")
    private List<BookReviewLikeEntity> reviewLikeList = new ArrayList<>();

}
