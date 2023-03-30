package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.books.BookReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookReviewLikeRepository extends JpaRepository<BookReviewLike, Integer> {

    @Modifying
    @Transactional
    @Query(value = "insert into book_review_like(review_id, user_id, value) " +
            "values (?1, ?2, ?3) " +
            "on conflict(review_id, user_id) do update set review_id = ?1, user_id = ?2, value = ?3", nativeQuery = true)
    void insertOrUpdateBookReviewLike(int reviewId, int userId, short value);

}
