package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dto.BookStatusRequestDto;
import com.example.mybookshopapp.dto.ResponseResultDto;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.links.BookCodeType;
import com.example.mybookshopapp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class CookieBooksService {

    private static final String CART_COOKIE_NAME = "cartContent";
    private static final String KEPT_COOKIE_NAME = "keptContent";

    private final BooksRatingAndPopularityService booksRatingAndPopularityService;
    private final BookRepository bookRepository;
    private final HttpServletResponse response;
    private final HttpServletRequest request;

    @Autowired
    public CookieBooksService(BooksRatingAndPopularityService booksRatingAndPopularityService, BookRepository bookRepository,
                              HttpServletResponse response, HttpServletRequest request) {
        this.booksRatingAndPopularityService = booksRatingAndPopularityService;
        this.bookRepository = bookRepository;
        this.response = response;
        this.request = request;
    }

    public ResponseResultDto changeBookStatus(BookStatusRequestDto dto) {
        Cookie cartCookie = getCookieByName(CART_COOKIE_NAME);
        Cookie keptCookie = getCookieByName(KEPT_COOKIE_NAME);

        String[] slugs = dto.getBooksIds().replace("[", "").replace("]", "").split(", ");

        for (String slug : slugs) {
            switch (dto.getStatus()) {
                case CART: {
                    addBookToCookie(slug, cartCookie);
                    booksRatingAndPopularityService.changePopularity(slug, 0.7);
                    removeBookFromCookie(slug, keptCookie, -0.4);
                    break;
                }
                case KEPT: {
                    addBookToCookie(slug, keptCookie);
                    booksRatingAndPopularityService.changePopularity(slug, 0.4);
                    removeBookFromCookie(slug, cartCookie, -0.7);
                    break;
                }
                case UNLINK: {
                    System.out.println("UNLINK");
                    removeBookFromCookie(slug, cartCookie, -0.7);
                    removeBookFromCookie(slug, keptCookie, -0.4);
                    break;
                }
                default:
                    break;
            }
        }

        response.addCookie(keptCookie);
        response.addCookie(cartCookie);

        return new ResponseResultDto(true);
    }

    public BookCodeType getBookStatus(String slug) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getValue() != null && !cookie.getValue().isEmpty()) {
                    List<String> slugList = getSlugBooksFromCookie(cookie.getValue());
                    if (slugList.contains(slug))
                        return cookie.getName().equals("cartContent") ? BookCodeType.CART : BookCodeType.KEPT;
                }
            }
        }
        return BookCodeType.UNLINK;
    }

    private void removeBookFromCookie(String slug, Cookie cookie, Double value) {
        if (cookie.getValue() != null && !cookie.getValue().isEmpty()) {
            List<String> slugList = getSlugBooksFromCookie(cookie.getValue());
            if (slugList.remove(slug))
                booksRatingAndPopularityService.changePopularity(slug, value);
            cookie.setValue(String.join("/", slugList));
        }
    }

    public List<String> getSlugBooksFromCookie(String cookie) {
        return cookie == null || cookie.isEmpty()
                ? Collections.emptyList() : new ArrayList<>(Arrays.asList(cookie.split("/")));
    }

    public List<Book> getBooksFromCookie(String cookie) {
        return cookie == null || cookie.isEmpty()
                ? Collections.emptyList() : bookRepository.findBookEntitiesBySlugIn(Arrays.asList(cookie.split("/")));
    }

    private void addBookToCookie(String slug, Cookie cookie) {
        if (cookie.getValue() == null || cookie.getValue().isEmpty()) {
            cookie.setPath("/");
            cookie.setValue(slug);
        } else if (!cookie.getValue().contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cookie.getValue()).add(slug);
            cookie.setValue(stringJoiner.toString());
        }
    }

    private Cookie createCookie(String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    private Cookie getCookieByName(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(name)) {
                    cookie.setPath("/");
                    return cookie;
                }
            }
        }
        return createCookie(name);
    }

    public Map<BookCodeType, List<Book>> getBooksFromCookies(String cartContent, String keptContent) {
        Map<BookCodeType, List<Book>> books = new HashMap<>();
        books.put(BookCodeType.CART, getBooksFromCookie(cartContent));
        books.put(BookCodeType.KEPT, getBooksFromCookie(keptContent));
        return books;
    }
}
