package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.books.BookReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookReviewRepository extends ModelRepository<BookReview> {

    Optional<BookReview> findByText(String text);

    Page<BookReview> findByStatus(short status, Pageable pageable);

    @Query("select count(b) from BookReview b where b.status = ?1")
    long countByStatus(short status);

}
