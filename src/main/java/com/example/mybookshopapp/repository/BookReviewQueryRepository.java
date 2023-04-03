package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.books.BookReviewF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookReviewQueryRepository extends JpaRepository<BookReviewF, Integer> {

    @Query(value = "select * from get_reviews(?1, ?2)", nativeQuery = true)
    List<BookReviewF> getReviewByBookAndUser(String slug, int userId);

    @Query(value = "select * from get_reviews(?1, ?2) where id = ?3", nativeQuery = true)
    BookReviewF getReviewByBookAndUser(String slug, int userId, int reviewId);
}
