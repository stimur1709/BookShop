package com.example.mybookshopapp.service;

import com.example.mybookshopapp.repository.BookReviewLikeRepository;
import com.example.mybookshopapp.service.user.UserProfileService;
import com.example.mybookshopapp.util.MessageLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class BookRateReviewService {

    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final UserProfileService userProfileService;
    private final MessageLocale messageLocale;

    @Autowired
    public BookRateReviewService(BookReviewLikeRepository bookReviewLikeRepository,
                                 UserProfileService userProfileService, MessageLocale messageLocale) {
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.userProfileService = userProfileService;
        this.messageLocale = messageLocale;
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
        response.put("error", messageLocale.getMessage("message.onlyAuth"));
        return response;
    }

}
