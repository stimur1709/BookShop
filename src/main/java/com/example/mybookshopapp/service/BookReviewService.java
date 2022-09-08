package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.review.BookReview;
import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.repository.BookReviewRepository;
import com.example.mybookshopapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    public boolean saveBookReview(int bookId, String text) {
        Optional<Book> bookEntity = bookRepository.findById(bookId);
        Optional<User> user = userRepository.findById(1);
        if (bookEntity.isPresent() && user.isPresent()) {
            BookReview bookReview = new BookReview(bookEntity.get(), user.get(), text);
            bookEntity.get().getReviewList().add(bookReview);

            bookReviewRepository.save(bookReview);
            bookRepository.save(bookEntity.get());
            return true;
        }
        return false;
    }

    public List<BookReview> getBookReview(Book book) {
        return bookReviewRepository.getBookReviewEntitiesByBook(book, Sort.by(Sort.Direction.DESC, "rate"));
    }
}
