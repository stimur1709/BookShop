package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.book.review.BookReview;
import com.example.mybookshopapp.data.entity.book.review.BookReviewLike;
import com.example.mybookshopapp.data.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReviewLikeRepository extends JpaRepository<BookReviewLike, Integer> {

    BookReviewLike findByBookReviewAndUser(BookReview bookReview, User user);

}
