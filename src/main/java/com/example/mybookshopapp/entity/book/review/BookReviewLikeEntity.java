package com.example.mybookshopapp.entity.book.review;

import com.example.mybookshopapp.entity.user.UserEntity;
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
public class BookReviewLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "review_id")
    private BookReviewEntity bookReview;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "user_id")
    private UserEntity user;

    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "SMALLINT NOT NULL")
    private short value;
}
