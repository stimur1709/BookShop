package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.book.review.BookReview;
import com.example.mybookshopapp.model.book.review.BookReviewLike;
import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.repository.BookReviewLikeRepository;
import com.example.mybookshopapp.repository.BookReviewRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class BookRateReviewService {

    private final BookReviewRepository bookReviewRepository;
    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final BookRepository bookRepository;
    private final UserProfileService userProfileService;
    private final HttpServletRequest request;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    @Autowired
    public BookRateReviewService(BookReviewRepository bookReviewRepository, BookReviewLikeRepository bookReviewLikeRepository,
                                 BookRepository bookRepository, UserProfileService userProfileService, HttpServletRequest request,
                                 MessageSource messageSource, LocaleResolver localeResolver) {
        this.bookReviewRepository = bookReviewRepository;
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.bookRepository = bookRepository;
        this.userProfileService = userProfileService;
        this.request = request;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    public Map<String, Object> changeRateBookReview(int idReview, short value) {
        Map<String, Object> response = new HashMap<>();
        User user = userProfileService.getCurrentUser();
        BookReview review = bookReviewRepository.getById(idReview);
        if (user != null) {
            BookReviewLike reviewLike = bookReviewLikeRepository.findByBookReviewAndUser(review, user);
            if (reviewLike == null) {
                reviewLike = new BookReviewLike(review, user, value);
            } else {
                reviewLike.setValue(value);
            }
            bookReviewLikeRepository.save(reviewLike);
            response.put("result", true);
            return response;
        }
        response.put("result", false);
        response.put("error", messageSource.getMessage("message.onlyAuth", null, localeResolver.resolveLocale(request)));
        return response;
    }

    private long getLikesReviewsOfBook(int bookId) {
        return bookRepository.getById(bookId).getReviewList().stream().mapToLong(BookReview::getLikes).sum();
    }

    private long getDislikesReviewsOfBook(int bookId) {
        return bookRepository.getById(bookId).getReviewList().stream().mapToLong(BookReview::getDislikes).sum();
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
