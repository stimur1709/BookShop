package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.entity.books.BookRating;
import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.data.outher.ResponseResultDto;
import com.example.mybookshopapp.repository.BookRatingRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BooksRatingAndPopularityService {

    private final BookRatingRepository bookRatingRepository;
    private final UserProfileService userProfileService;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;
    private final HttpServletRequest request;

    @Autowired
    public BooksRatingAndPopularityService(BookRatingRepository bookRatingRepository, UserProfileService userProfileService,
                                           MessageSource messageSource, LocaleResolver localeResolver, HttpServletRequest request) {
        this.bookRatingRepository = bookRatingRepository;
        this.userProfileService = userProfileService;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
        this.request = request;
    }

    public Map<Integer, Long> getSizeofRatingValue(int idBook) {
        List<BookRating> bookRatings = bookRatingRepository.findByBook_id(idBook);
        return bookRatings.stream().collect(Collectors.groupingBy(BookRating::getRating, Collectors.counting()));
    }

    public ResponseResultDto changeRateBook(int bookId, int value) {
        if (!userProfileService.isAuthenticatedUser()) {
            String message = messageSource.getMessage("message.onlyAuth", null, localeResolver.resolveLocale(request));
            return new ResponseResultDto(false, message);
        }
        User user = userProfileService.getCurrentUser();
        int size = bookRatingRepository.updateRating(value, bookId, user.getId());
        String message = messageSource.getMessage("message.changeRate", null, localeResolver.resolveLocale(request));
        return new ResponseResultDto(true, message, size, getSizeofRatingValue(bookId));
    }

}
