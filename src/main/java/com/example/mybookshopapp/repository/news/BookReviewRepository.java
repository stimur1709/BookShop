package com.example.mybookshopapp.repository.news;

import com.example.mybookshopapp.data.entity.books.BookReview;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookReviewRepository extends ModelRepository<BookReview> {

    Optional<BookReview> findByText(String text);

}
