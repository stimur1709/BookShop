package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.book.Book;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:/application-test.yaml")
@Slf4j
class BookRepositoryTest {

    private final BookRepository bookRepository;

    @Autowired
    BookRepositoryTest(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Test
    void findBookByTitle() {
        String title = "java";

        Pageable pageable = PageRequest.of(0, 100);
        List<Book> books = bookRepository.findBookEntityByTitleContainingAllIgnoreCase(title, pageable).getContent();

        assertNotNull(books);
        assertFalse(books.isEmpty());

        books.stream().map(Book::getTitle).forEach(log::info);

    }
}