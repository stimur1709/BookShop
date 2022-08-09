package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.book.review.BookReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReviewEntity, Integer> {
}
