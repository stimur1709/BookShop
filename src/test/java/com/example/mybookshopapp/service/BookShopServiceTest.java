package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dto.BookStatusRequestDto;
import com.example.mybookshopapp.dto.ResponseResultDto;
import com.example.mybookshopapp.model.book.links.BookCodeType;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.util.GeneratorCookie;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource("/application.yaml")
@Slf4j
@DisplayName("Популярность книг")
class BookShopServiceTest {

    private final GeneratorCookie generatorCookie;
    private final BookShopService bookShopService;
    private final BookRepository bookRepository;

    @Autowired
    BookShopServiceTest(GeneratorCookie generatorCookie, BookShopService bookShopService, BookRepository bookRepository) {
        this.generatorCookie = generatorCookie;
        this.bookShopService = bookShopService;
        this.bookRepository = bookRepository;
    }

//    @BeforeEach
//    void setUp() {
//    }

    @AfterEach
    void tearDown() {
//        List<Book> books = bookRepository.findAll();
//        books.forEach(book -> book.setPopularity(0.0));
//        bookRepository.saveAll(books);
    }

    @Test
    @DisplayName("Изменение популярности книг через cookie")
    void changeBookStatus() {
        for (int i = 0; i < 5; i++) {
            generatePopularity(i, (i + 1) * 10);
            System.out.println((i + 1) * 10);
        }
    }

    void generatePopularity(int offset, int count) {
        Cookie[] cookies = generatorCookie.createCookies(offset, count);
        for (Cookie cookie : cookies) {
            BookCodeType status = cookie.getName().equals("keptContent") ? BookCodeType.KEPT : BookCodeType.CART;
            BookStatusRequestDto dto = new BookStatusRequestDto(cookie.getValue(), status);
            ResponseResultDto response = bookShopService.changeBookStatus(dto);
            assertTrue(response.getResult());
        }
    }
}