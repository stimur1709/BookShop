package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.book.review.BookReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReviewLikeRepository extends JpaRepository<BookReviewLike, Integer> {
}
