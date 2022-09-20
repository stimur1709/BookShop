package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.book.BookRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRatingRepository extends JpaRepository<BookRating, Integer> {
    Optional<BookRating> findByBook_IdAndScore(int id, int score);

}
