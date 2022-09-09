package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dto.BooksStatusRequestDto;
import com.example.mybookshopapp.dto.ResponseResultDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class CookieService {
    private static final String CART_COOKIE_NAME = "cartContent";
    private static final String KEPT_COOKIE_NAME = "keptContent";

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

    public ResponseResultDto changeBookStatus(HttpServletResponse response, Cookie[] cookies, BooksStatusRequestDto dto) {
        Cookie cartCookie = getCookieByName(cookies, CART_COOKIE_NAME);
        Cookie keptCookie = getCookieByName(cookies, KEPT_COOKIE_NAME);
        String slug = dto.getBooksIds();

        switch (dto.getStatus()) {
            case CART: {
                addBookToCookie(slug, cartCookie);
                removeBookFromCookie(slug, keptCookie);
                response.addCookie(cartCookie);
                break;
            }
            case KEPT: {
                addBookToCookie(slug, keptCookie);
                removeBookFromCookie(slug, cartCookie);
                response.addCookie(keptCookie);
                break;
            }
            case UNLINK: {
                removeBookFromCookie(slug, cartCookie);
                removeBookFromCookie(slug, keptCookie);
                break;
            }
            default:
                System.out.println(3);
                return new ResponseResultDto(false);
        }
        return new ResponseResultDto(true);
    }

    private void removeBookFromCookie(String slug, Cookie cookie) {
        if (cookie.getValue() != null && !cookie.getValue().isEmpty()) {
            List<String> slugList = getBooksFromCookie(cookie.getValue());
            slugList.remove(slug);
            cookie.setValue(String.join("/", slugList));
        }
    }

    public List<String> getBooksFromCookie(String cookie) {
        if (cookie == null || cookie.isEmpty())
            return Collections.emptyList();
        else
            return new ArrayList<>(Arrays.asList(cookie.split("/")));
    }

    private void addBookToCookie(String slug, Cookie cookie) {
        if (cookie.getValue() == null || cookie.getValue().isEmpty())
            cookie.setValue(slug);
        else if (!cookie.getValue().contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cookie.getValue()).add(slug);
            cookie.setValue(stringJoiner.toString());
        }
    }
}
