package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.outher.ContactConfirmationPayload;
import com.example.mybookshopapp.data.entity.books.Book;
import com.example.mybookshopapp.data.entity.books.BookReview;
import com.example.mybookshopapp.data.entity.books.BookReviewLike;
import com.example.mybookshopapp.data.entity.books.BookReviewF;
import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.repository.*;
import com.example.mybookshopapp.repository.BookQueryRepository;
import com.example.mybookshopapp.repository.BookReviewRepository;
import com.example.mybookshopapp.service.user.UserAuthService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@Slf4j
@DisplayName("Рейтинг отзывов")
class BookRateReviewServiceTest {

    @Autowired
    private BookReviewQueryRepository bookReviewQueryRepository;

    private final BookQueryRepository bookQueryRepository;
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
    BookRateReviewServiceTest(BookQueryRepository bookQueryRepository, BookRateReviewService bookRateReviewService, BookReviewRepository bookReviewRepository, BookRepository bookRepository, UserRepository userRepository, UserAuthService userAuthService, BookReviewLikeRepository bookReviewLikeRepository) {
        this.bookQueryRepository = bookQueryRepository;
        this.bookRateReviewService = bookRateReviewService;
        this.bookReviewRepository = bookReviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.userAuthService = userAuthService;
        this.bookReviewLikeRepository = bookReviewLikeRepository;
    }

    @BeforeEach
    void setUp() {
        book = bookRepository.findBookEntityBySlug("d451ebcd-bf4d-4073-9f4a-10232478bff3");
        user = userRepository.getByHash("stimurstimurstimurs");
        review = bookReviewRepository.save(new BookReview(book.getId(), user.getId(), "Норм книжка"));
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
        BookReviewF review = bookReviewQueryRepository.getReviewByBookAndUser(book.getSlug(), user.getId(), this.review.getId());
        assertNotNull(review);
        long likes = review.getLikes();
        long dislikes = review.getDislikes();
        assertEquals(0, dislikes);
        assertEquals(1, likes);
        response = bookRateReviewService.changeRateBookReview(this.review.getId(), (short) -1);
        assertEquals(true, response.get("result"));
        review = bookReviewQueryRepository.getReviewByBookAndUser(book.getSlug(), user.getId(), this.review.getId());
        assertNotNull(review);
        likes = review.getLikes();
        dislikes = review.getDislikes();
        assertEquals(1, dislikes);
        assertEquals(0, likes);
    }

    @Test
    @DisplayName("Расчет рейтинга книги по отзывам")
    void ratingCalculation() {
        List<User> users = userRepository.findAll();

        for (int i = 0; i < 20; i++) {
            BookReview newReview = bookReviewRepository.save(new BookReview(book.getId(), user.getId(), "kroiqwpnrifniqe " + i));
            for (User user1 : users) {
                bookReviewLikeRepository.insertOrUpdateBookReviewLike(newReview.getId(), user1.getId(), (short) 1);
            }
        }

        int rate = bookQueryRepository.getBook(1, book.getSlug()).getRateReview();
        assertEquals(5, rate);

        List<BookReviewLike> bookReviewLikes = bookReviewLikeRepository.findAll();
        bookReviewLikes.forEach(bookReviewLike -> bookReviewLike.setValue((short) -1));
        bookReviewLikeRepository.saveAll(bookReviewLikes);
        rate = bookQueryRepository.getBook(1, book.getSlug()).getRateReview();
        assertEquals(1, rate);
    }
}