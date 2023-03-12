package com.example.mybookshopapp.data.entity.book.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "book_review_like")
@Schema(description = "Лайки и дизлайки отзывов")
public class BookReviewLike {

    @EmbeddedId
    private KeyBookReviewLike id;

    private LocalDateTime time;

    @Column(columnDefinition = "SMALLINT NOT NULL")
    private short value;

    public BookReviewLike() {
    }
}
