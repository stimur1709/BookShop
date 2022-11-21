package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.review.BookReview;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Integer> {

    List<BookReview> getBookReviewEntitiesByBook(Book book, Sort sort);

}
