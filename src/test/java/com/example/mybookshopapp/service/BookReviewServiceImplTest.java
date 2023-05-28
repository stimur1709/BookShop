package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.outher.ContactConfirmationPayload;
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


@SpringBootTest
@TestPropertySource("/application-test.yaml")
@Slf4j
@DisplayName("Отзывы")
class BookReviewServiceImplTest {

    private final BookReviewRepository bookReviewRepository;
    private final UserAuthService userAuthService;
    private final BookReviewServiceImpl bookReviewServiceImpl;

    @Autowired
    BookReviewServiceImplTest(BookReviewRepository bookReviewRepository, UserAuthService userAuthService, BookReviewServiceImpl bookReviewServiceImpl) {
        this.bookReviewRepository = bookReviewRepository;
        this.userAuthService = userAuthService;
        this.bookReviewServiceImpl = bookReviewServiceImpl;
    }

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
        bookReviewRepository.deleteAll();
    }

    @Test
    @DisplayName("Отправка отзыва авторизованным пользователем")
    void saveBookReviewAuthUser() {
        String text = "Хорошая книга";
        ContactConfirmationPayload payload = new ContactConfirmationPayload();
        payload.setContact("stimur1709@mail.ru");
        payload.setCode("123456789");
        userAuthService.jwtLogin(payload);
//        ResponseResultDto response = bookReviewServiceImpl.saveBookReview(1, text);
//        assertEquals(response.getText(), text);
//        assertTrue(bookReviewRepository.findByText(text).isPresent());
    }

    @Test
    @DisplayName("Отправка отзыва не авторизованного пользователя")
    void saveBookReviewNoAuthUser() {
        String text = "Хорошая книга";
//        ResponseResultDto response = bookReviewServiceImpl.saveBookReview(1, text);
//        assertFalse(response.getResult());
    }

}