package com.example.mybookshopapp.data.entity.book.review;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class KeyBookReviewLike implements Serializable {

    private static final long serialVersionUID = -989914673137445796L;
    @Column(name = "user_id")
    private int userId;

    @Column(name = "review_id")
    private int reviewId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyBookReviewLike that = (KeyBookReviewLike) o;
        return userId == that.userId && reviewId == that.reviewId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, reviewId);
    }
}
