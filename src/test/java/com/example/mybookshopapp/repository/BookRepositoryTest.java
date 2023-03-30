package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.books.Book;
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
@TestPropertySource("/application-test.yaml")
@Slf4j
class BookRepositoryTest {

    private final BookRepository bookRepository;

    @Autowired
    BookRepositoryTest(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Test
    void findBookByTitle() {
        String title = "марс";

        Pageable pageable = PageRequest.of(0, 100);
        List<Book> books = bookRepository.findBookEntityByTitleContainingAllIgnoreCase(title, pageable).getContent();

        books.stream().map(Book::getTitle).forEach(log::info);

        assertNotNull(books);
        assertFalse(books.isEmpty());
    }
    
}