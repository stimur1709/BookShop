package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.book.review.BookReview;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Integer> {

    List<BookReview> getBookReviewEntitiesByBook_Slug(String slug, Sort sort);

    Optional<BookReview> findByText(String text);

}
