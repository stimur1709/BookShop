package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dto.BooksStatusRequestDto;
import com.example.mybookshopapp.dto.ResponseResultDto;
import com.example.mybookshopapp.dto.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class CookieService {
    private static final String CART_COOKIE_NAME = "cartContent";
    private static final String KEPT_COOKIE_NAME = "keptContent";

    private final BooksRatingAndPopularityService booksRatingAndPopularityService;

    @Autowired
    public CookieService(BooksRatingAndPopularityService booksRatingAndPopularityService) {
        this.booksRatingAndPopularityService = booksRatingAndPopularityService;
    }

    public ResponseResultDto changeBookStatus(HttpServletResponse response, Cookie[] cookies, BooksStatusRequestDto dto) {
        Cookie cartCookie = getCookieByName(cookies, CART_COOKIE_NAME);
        Cookie keptCookie = getCookieByName(cookies, KEPT_COOKIE_NAME);

        String slug = dto.getBooksIds();

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
                removeBookFromCookie(slug, cartCookie, -0.7);
                removeBookFromCookie(slug, keptCookie, -0.4);
                break;
            }
            default:
                return new ResponseResultDto(false);
        }

        response.addCookie(keptCookie);
        response.addCookie(cartCookie);

        return new ResponseResultDto(true);
    }

    public Status getBookStatus(String slug, Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getValue() != null && !cookie.getValue().isEmpty()) {
                    List<String> slugList = getBooksFromCookie(cookie.getValue());
                    if (slugList.contains(slug))
                        return cookie.getName().equals("cartContent") ? Status.CART : Status.KEPT;
                }
            }
        }
        return Status.UNLINK;
    }

    private void removeBookFromCookie(String slug, Cookie cookie, Double value) {
        if (cookie.getValue() != null && !cookie.getValue().isEmpty()) {
            List<String> slugList = getBooksFromCookie(cookie.getValue());
            if (slugList.remove(slug))
                booksRatingAndPopularityService.changePopularity(slug, value);
            cookie.setValue(String.join("/", slugList));
        }
    }

    public List<String> getBooksFromCookie(String cookie) {
        return cookie == null || cookie.isEmpty()
                ? Collections.emptyList() : new ArrayList<>(Arrays.asList(cookie.split("/")));
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

    private Cookie getCookieByName(Cookie[] cookies, String name) {
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
}