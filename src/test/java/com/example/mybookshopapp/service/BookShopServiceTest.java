package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.outher.BookStatusRequestDto;
import com.example.mybookshopapp.data.outher.ContactConfirmationPayload;
import com.example.mybookshopapp.data.entity.books.Book;
import com.example.mybookshopapp.data.entity.links.BookCodeType;
import com.example.mybookshopapp.repository.Book2UserRepository;
import com.example.mybookshopapp.repository.BookRepository;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource("/application-test.yaml")
@Slf4j
@DisplayName("Популярность книг")
class BookShopServiceTest {

    private final BookShopService bookShopService;
    private final BookRepository bookRepository;
    private final UserAuthService userAuthService;
    private final Book2UserRepository book2UserRepository;

    @Autowired
    BookShopServiceTest(BookShopService bookShopService, BookRepository bookRepository, UserAuthService userAuthService, Book2UserRepository book2UserRepository) {
        this.bookShopService = bookShopService;
        this.bookRepository = bookRepository;
        this.userAuthService = userAuthService;
        this.book2UserRepository = book2UserRepository;
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        List<Book> books = bookRepository.findAll();
        books.forEach(book -> book.setPopularity(0.0));
        bookRepository.saveAll(books);
        book2UserRepository.deleteAll();
    }

    @Test
    @DisplayName("Изменение популярности книг. Один пользователь")
    void changeBookStatusWithAuthUser() {
        ContactConfirmationPayload payload = new ContactConfirmationPayload();
        payload.setContact("stimur1709@mail.ru");
        payload.setCode("123456789");
        userAuthService.jwtLogin(payload);
        List<Book> books = bookRepository.findAll();
        List<String> collect = books.stream().map(Book::getSlug).collect(Collectors.toList());
        bookShopService.changeBookStatus(new BookStatusRequestDto(collect.toString(), BookCodeType.CART));
        books = bookRepository.findAll();
        double sumCart = books.stream().mapToDouble(Book::getPopularity).sum();
        bookShopService.changeBookStatus(new BookStatusRequestDto(collect.toString(), BookCodeType.KEPT));
        books = bookRepository.findAll();
        double sumKept = books.stream().mapToDouble(Book::getPopularity).sum();
        assertEquals(String.valueOf(books.size() * 0.7 - books.size() * 0.4).substring(0, 4), String.valueOf(sumCart - sumKept).substring(0, 4));

    }

}