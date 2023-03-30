package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.books.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Integer> {

    Optional<BookReview> findByText(String text);

}
