package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.ResponseResultDto;
import com.example.mybookshopapp.data.entity.book.Book;
import com.example.mybookshopapp.data.entity.book.review.BookReview;
import com.example.mybookshopapp.data.entity.book.review.BookReviewQuery;
import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.repository.BookReviewQueryRepository;
import com.example.mybookshopapp.repository.BookReviewRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class BookReviewService {

    private final BookRepository bookRepository;
    private final BookReviewRepository bookReviewRepository;
    private final LocaleResolver localeResolver;
    private final MessageSource messageSource;
    private final HttpServletRequest request;
    private final UserProfileService userProfileService;
    private final BookReviewQueryRepository bookReviewQueryRepository;

    @Autowired
    public BookReviewService(BookRepository bookRepository,
                             BookReviewRepository bookReviewRepository, LocaleResolver localeResolver,
                             MessageSource messageSource, HttpServletRequest request, UserProfileService userProfileService,
                             BookReviewQueryRepository bookReviewQueryRepository) {
        this.bookRepository = bookRepository;
        this.userProfileService = userProfileService;
        this.bookReviewRepository = bookReviewRepository;
        this.localeResolver = localeResolver;
        this.messageSource = messageSource;
        this.request = request;
        this.bookReviewQueryRepository = bookReviewQueryRepository;
    }

    public ResponseResultDto saveBookReview(int bookId, String text) {
        Optional<Book> bookEntity = bookRepository.findById(bookId);
        User user = userProfileService.getCurrentUser();
        if (bookEntity.isPresent() && user != null) {
            BookReview bookReview = new BookReview(bookEntity.get(), user, text);
            BookReview review = bookReviewRepository.save(bookReview);
            String name = user.getFirstname() + ' ' + user.getLastname();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm", localeResolver.resolveLocale(request));
            String date = simpleDateFormat.format(review.getTime());
            return new ResponseResultDto(true, review.getText(), name, date);
        }
        return new ResponseResultDto(false, messageSource.getMessage("message.reviewEmpty", null, localeResolver.resolveLocale(request)));
    }

    public List<BookReviewQuery> getBookReview(String slug) {
        User user = userProfileService.getCurrentUser();
        return bookReviewQueryRepository.getReviewsByBookAndUser(slug, user.getId());
    }
}
