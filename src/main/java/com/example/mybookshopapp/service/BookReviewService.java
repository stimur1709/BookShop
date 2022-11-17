package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dto.ResponseResultDto;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.review.BookReview;
import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.repository.BookReviewRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
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

    @Autowired
    public BookReviewService(BookRepository bookRepository,
                             BookReviewRepository bookReviewRepository, LocaleResolver localeResolver,
                             MessageSource messageSource, HttpServletRequest request, UserProfileService userProfileService) {
        this.bookRepository = bookRepository;
        this.userProfileService = userProfileService;
        this.bookReviewRepository = bookReviewRepository;
        this.localeResolver = localeResolver;
        this.messageSource = messageSource;
        this.request = request;
    }

    public ResponseResultDto saveBookReview(int bookId, String text) {
        Optional<Book> bookEntity = bookRepository.findById(bookId);
        User user = userProfileService.getCurrentUser();
        if (bookEntity.isPresent()) {
            BookReview bookReview = new BookReview(bookEntity.get(), user, text);
            bookEntity.get().getReviewList().add(bookReview);
            BookReview review = bookReviewRepository.save(bookReview);
            bookRepository.save(bookEntity.get());
            String name = user.getFirstname() + ' ' + user.getLastname();
            System.out.println(review.getTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm", localeResolver.resolveLocale(request));
            String date = simpleDateFormat.format(review.getTime());
            return new ResponseResultDto(true, review.getText(), name, date);
        }
        return new ResponseResultDto(false, messageSource.getMessage("message.reviewShort", null, localeResolver.resolveLocale(request)));
    }

    public List<BookReview> getBookReview(Book book) {
        return bookReviewRepository.getBookReviewEntitiesByBook(book, Sort.by(Sort.Direction.DESC, "rate"));
    }
}
