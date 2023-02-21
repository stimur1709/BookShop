package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.book.review.BookReviewQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookReviewQueryRepository extends JpaRepository<BookReviewQuery, Integer> {

    @Query(value = "select * from get_reviews(?1, ?2)", nativeQuery = true)
    List<BookReviewQuery> getReviewsByBookAndUser(String slug, int userId);
}
