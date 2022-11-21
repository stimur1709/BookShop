package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dto.ResponseResultDto;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.BookRating;
import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.repository.BookRatingRepository;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BooksRatingAndPopularityService {

    private final BookRatingRepository bookRatingRepository;
    private final BookRepository bookRepository;
    private final UserProfileService userProfileService;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;
    private final HttpServletRequest request;

    @Autowired
    public BooksRatingAndPopularityService(BookRatingRepository bookRatingRepository,
                                           BookRepository bookRepository, UserProfileService userProfileService,
                                           MessageSource messageSource, LocaleResolver localeResolver, HttpServletRequest request) {
        this.bookRatingRepository = bookRatingRepository;
        this.bookRepository = bookRepository;
        this.userProfileService = userProfileService;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
        this.request = request;
    }

    public void changePopularity(Book book, Double value) {
        book.setPopularity(book.getPopularity() + value);
        bookRepository.save(book);
    }

    public void changePopularity(String slug, Double value) {
        Book book = bookRepository.findBookEntityBySlug(slug);
        book.setPopularity(book.getPopularity() + value);
        bookRepository.save(book);
    }

    public Map<Integer, Long> getSizeofRatingValue(int idBook) {
        List<BookRating> bookRatings = bookRatingRepository.findByBook_id(idBook);
        return bookRatings.stream().collect(Collectors.groupingBy(BookRating::getRating, Collectors.counting()));
    }

    public int getRateByUserAndBook(Book book) {
        return bookRatingRepository.findByBookAndUser(book, userProfileService.getCurrentUser())
                .map(BookRating::getRating).orElse(0);
    }

    public ResponseResultDto changeRateBook(int bookId, int value) {
        User user = userProfileService.getCurrentUser();
        Book book = bookRepository.getById(bookId);
        if (user == null) {
            String message = messageSource.getMessage("message.onlyAuth", null, localeResolver.resolveLocale(request));
            return new ResponseResultDto(false, message);
        }
        Optional<BookRating> bookRating = bookRatingRepository.findByBookAndUser(book, user);
        if (bookRating.isPresent()) {
            bookRating.get().setRating(value);
            bookRatingRepository.save(bookRating.get());
        } else {
            bookRatingRepository.save(new BookRating(value, book, user));
        }
        String message = messageSource.getMessage("message.changeRate", null, localeResolver.resolveLocale(request));
        return new ResponseResultDto(true, message, book.getBookRatingList().size(), getSizeofRatingValue(bookId));
    }

}
