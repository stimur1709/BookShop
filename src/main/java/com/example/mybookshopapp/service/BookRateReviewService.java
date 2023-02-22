package com.example.mybookshopapp.service;

import com.example.mybookshopapp.repository.BookReviewLikeRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class BookRateReviewService {

    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final UserProfileService userProfileService;
    private final HttpServletRequest request;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    @Autowired
    public BookRateReviewService(BookReviewLikeRepository bookReviewLikeRepository,
                                 UserProfileService userProfileService, HttpServletRequest request,
                                 MessageSource messageSource, LocaleResolver localeResolver) {
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.userProfileService = userProfileService;
        this.request = request;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    @Transactional
    public Map<String, Object> changeRateBookReview(int idReview, short value) {
        Map<String, Object> response = new HashMap<>();
        if (userProfileService.isAuthenticatedUser()) {
            bookReviewLikeRepository.insertOrUpdateBookReviewLike(idReview, userProfileService.getCurrentUser().getId(), value);
            response.put("result", true);
            return response;
        }
        response.put("result", false);
        response.put("error", messageSource.getMessage("message.onlyAuth", null, localeResolver.resolveLocale(request)));
        return response;
    }

}
