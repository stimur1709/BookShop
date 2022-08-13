package com.example.mybookshopapp.service;

import com.example.mybookshopapp.entity.book.review.BookReviewEntity;
import com.example.mybookshopapp.entity.book.review.BookReviewLikeEntity;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.repository.BookReviewLikeRepository;
import com.example.mybookshopapp.repository.BookReviewRepository;
import com.example.mybookshopapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookRateReviewService {

    private final BookReviewRepository bookReviewRepository;
    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BookRateReviewService(BookReviewRepository bookReviewRepository, BookReviewLikeRepository bookReviewLikeRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.bookReviewRepository = bookReviewRepository;
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public boolean changeRateBookReview(int idReview, short value) {
        BookReviewEntity review = bookReviewRepository.getById(idReview);
        if (value == 1 || value == -1) {
            BookReviewLikeEntity bookReviewLike = new BookReviewLikeEntity(review, userRepository.getById(1), value);
            review.getReviewLikeList().add(bookReviewLike);
            bookReviewLikeRepository.save(bookReviewLike);
            return true;
        }
        return false;
    }

    private long getLikesReviewsOfBook(int bookId) {
        return bookRepository.getById(bookId).getReviewList().stream().mapToLong(BookReviewEntity::getLikes).sum();
    }

    private long getDislikesReviewsOfBook(int bookId) {
        return bookRepository.getById(bookId).getReviewList().stream().mapToLong(BookReviewEntity::getDislikes).sum();
    }

    private long differenceLikesAndDislikes(int bookId) {
        return getLikesReviewsOfBook(bookId) - getDislikesReviewsOfBook(bookId);
    }

    public int ratingCalculation(int bookId) {
        long difference = differenceLikesAndDislikes(bookId);
        long size = getLikesReviewsOfBook(bookId) + getDislikesReviewsOfBook(bookId);
        if (getLikesReviewsOfBook(bookId) == 0 && getDislikesReviewsOfBook(bookId) == 0) return 0;
        if (difference < size * 0.2) return 1;
        if (difference >= size * 0.2 && difference < size * 0.4) return 2;
        if (difference >= size * 0.4 && difference < size * 0.6) return 3;
        if (difference >= size * 0.6 && difference < size * 0.8) return 4;
        else return 5;
    }
}
