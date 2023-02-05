package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.data.entity.book.Book;
import com.example.mybookshopapp.data.entity.book.review.BookReview;
import com.example.mybookshopapp.data.entity.book.review.BookReviewLike;
import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.repository.BookReviewLikeRepository;
import com.example.mybookshopapp.repository.BookReviewRepository;
import com.example.mybookshopapp.repository.UserRepository;
import com.example.mybookshopapp.service.userService.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource("/application-test.yaml")
@Slf4j
@DisplayName("Рейтинг отзывов")
class BookRateReviewServiceTest {

    private final BookRateReviewService bookRateReviewService;
    private final BookReviewRepository bookReviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserAuthService userAuthService;
    private final BookReviewLikeRepository bookReviewLikeRepository;

    private BookReview review;
    private Book book;
    private User user;

    @Autowired
    BookRateReviewServiceTest(BookRateReviewService bookRateReviewService, BookReviewRepository bookReviewRepository, BookRepository bookRepository, UserRepository userRepository, UserAuthService userAuthService, BookReviewLikeRepository bookReviewLikeRepository) {
        this.bookRateReviewService = bookRateReviewService;
        this.bookReviewRepository = bookReviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.userAuthService = userAuthService;
        this.bookReviewLikeRepository = bookReviewLikeRepository;
    }

    @BeforeEach
    void setUp() {
        book = bookRepository.findBookEntityBySlug("zcjbamvddqipljkrzoj");
        user = userRepository.getByHash("stimurstimurstimurs");
        review = bookReviewRepository.save(new BookReview(book, user, "Норм книжка"));
    }

    @AfterEach
    void tearDown() {
        bookReviewLikeRepository.deleteAll();
        bookReviewRepository.deleteAll();
    }

    @Test
    @DisplayName("Лайк или дизлайк отзыву - один пользователь")
    void changeRateBookReview() {
        ContactConfirmationPayload payload = new ContactConfirmationPayload();
        payload.setContact("stimur1709@mail.ru");
        payload.setCode("123456789");
        userAuthService.jwtLogin(payload);
        Map<String, Object> response = bookRateReviewService.changeRateBookReview(review.getId(), (short) 1);
        assertEquals(true, response.get("result"));
        Optional<BookReview> reviewOptional = bookReviewRepository.findById(review.getId());
        assertTrue(reviewOptional.isPresent());
        long likes = reviewOptional.get().getLikes();
        long dislikes = reviewOptional.get().getDislikes();
        assertEquals(0, dislikes);
        assertEquals(1, likes);
        response = bookRateReviewService.changeRateBookReview(review.getId(), (short) -1);
        assertEquals(true, response.get("result"));
        reviewOptional = bookReviewRepository.findById(review.getId());
        assertTrue(reviewOptional.isPresent());
        likes = reviewOptional.get().getLikes();
        dislikes = reviewOptional.get().getDislikes();
        assertEquals(1, dislikes);
        assertEquals(0, likes);
    }

    @Test
    @DisplayName("Расчет рейтинга книги по отзывам")
    void ratingCalculation() {
        List<User> users = userRepository.findAll();

        for (int i = 0; i < 20; i++) {
            BookReview newReview = bookReviewRepository.save(new BookReview(book, user, "kroiqwpnrifniqe " + i));
            for (User user1 : users) {
                bookReviewLikeRepository.save(new BookReviewLike(newReview, user1, (short) 1));
            }
        }

        int rate = bookRateReviewService.ratingCalculation(book.getId());
        assertEquals(5, rate);

        List<BookReviewLike> bookReviewLikes = bookReviewLikeRepository.findAll();
        bookReviewLikes.forEach(bookReviewLike -> bookReviewLike.setValue((short) -1));
        bookReviewLikeRepository.saveAll(bookReviewLikes);
        rate = bookRateReviewService.ratingCalculation(book.getId());
        assertEquals(1, rate);
    }
}