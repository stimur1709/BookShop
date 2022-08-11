package com.example.mybookshopapp.service;

import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.entity.book.review.BookReviewEntity;
import com.example.mybookshopapp.entity.user.UserEntity;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.repository.BookReviewRepository;
import com.example.mybookshopapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookReviewService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookReviewRepository bookReviewRepository;

    @Autowired
    public BookReviewService(BookRepository bookRepository, UserRepository userRepository,
                             BookReviewRepository bookReviewRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.bookReviewRepository = bookReviewRepository;
    }

    public void saveBookReview(int bookId, String text) {
        Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
        Optional<UserEntity> user = userRepository.findById(1);
        if (bookEntity.isPresent() && user.isPresent()) {
            BookReviewEntity bookReview = new BookReviewEntity(bookEntity.get(), user.get(), text);
            bookEntity.get().getReviewList().add(bookReview);

            bookReviewRepository.save(bookReview);
            bookRepository.save(bookEntity.get());
        }
    }

    public List<BookReviewEntity> getBookReview(BookEntity book) {
        return bookReviewRepository.getBookReviewEntitiesByBook(book);
    }
}
