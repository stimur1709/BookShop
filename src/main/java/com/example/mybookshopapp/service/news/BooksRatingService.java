package com.example.mybookshopapp.service.news;

import com.example.mybookshopapp.data.entity.books.BookRating;
import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.data.outher.ResponseResultDto;
import com.example.mybookshopapp.repository.news.BookRatingRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import com.example.mybookshopapp.util.MessageLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BooksRatingService {

    private final BookRatingRepository bookRatingRepository;
    private final UserProfileService userProfileService;
    private final MessageLocale messageLocale;

    @Autowired
    public BooksRatingService(BookRatingRepository bookRatingRepository,
                              UserProfileService userProfileService,
                              MessageLocale messageLocale) {
        this.bookRatingRepository = bookRatingRepository;
        this.userProfileService = userProfileService;
        this.messageLocale = messageLocale;
    }

    public Map<Integer, Long> getSizeofRatingValue(int idBook) {
        List<BookRating> bookRatings = bookRatingRepository.findByBookId(idBook);
        return bookRatings.stream().collect(Collectors.groupingBy(BookRating::getRating, Collectors.counting()));
    }

    public ResponseResultDto changeRateBook(int bookId, int value) {
        if (!userProfileService.isAuthenticatedUser()) {
            String message = messageLocale.getMessage("message.onlyAuth");
            return new ResponseResultDto(false, message);
        }
        User user = userProfileService.getCurrentUser();
        int size = bookRatingRepository.updateRating(value, bookId, user.getId());
        String message = messageLocale.getMessage("message.changeRate");
        return new ResponseResultDto(true, message, size, getSizeofRatingValue(bookId));
    }

}
