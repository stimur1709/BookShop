package com.example.mybookshopapp.util;

import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.StringJoiner;

@Component
@Slf4j
public class GeneratorCookie {

    private final BookRepository bookRepository;

    @Autowired
    public GeneratorCookie(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Cookie[] createCookies(int count) {
        List<Book> books = bookRepository.findAll(PageRequest.of(0, count)).getContent();
        StringJoiner cart = new StringJoiner("/");
        StringJoiner kept = new StringJoiner("/");
        for (int i = 0; i < books.size(); i++) {
            if (i % 2 == 0) {
                cart.add(books.get(i).getSlug());
            } else {
                kept.add(books.get(i).getSlug());
            }
        }
        Cookie cartContent = new Cookie("cartContent", cart.toString());
        Cookie keptContent = new Cookie("keptContent", kept.toString());
        log.info("Созданы cookies: {}, {}", cartContent.getName(), keptContent.getName());
        return new Cookie[]{cartContent, keptContent};
    }
}
