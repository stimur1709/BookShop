package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dto.BooksStatusRequestDto;
import com.example.mybookshopapp.dto.ResponseResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Service
public class BookShopService {

    private final BooksRatingAndPopularityService booksRatingAndPopularityService;
    private final CookieService cookieService;

    @Autowired
    public BookShopService(BooksRatingAndPopularityService booksRatingAndPopularityService, CookieService cookieService) {
        this.booksRatingAndPopularityService = booksRatingAndPopularityService;
        this.cookieService = cookieService;
    }

    public ResponseResultDto changeBookStatus(HttpServletResponse response, HttpServletRequest request,
                                              BooksStatusRequestDto dto) {
        return cookieService.changeBookStatus(response, request.getCookies(), dto);
    }

    public List<String> getBooksFromCookie(String cookie) {
        return cookieService.getBooksFromCookie(cookie);
    }

    public void createCookie(String contents, String slug, HttpServletResponse response,
                             String cookieName, Model model, String attributeName) {
        if (contents == null || contents.equals("") && !slug.equals("1122")) {
            Cookie cookie = new Cookie(cookieName, slug);
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute(attributeName, false);
        } else if (!slug.equals("1122") && !contents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(contents).add(slug);
            Cookie cookie = new Cookie(cookieName, stringJoiner.toString());
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute(attributeName, false);
        }
    }

    public void removeCookie(String cookieContent, String slug, HttpServletResponse response, Model model,
                             String cookieName, String attributeName) {
        if (cookieContent != null && !cookieContent.equals("")) {
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cookieContent.split("/")));
            if (cookieBooks.contains(slug)) {
                cookieBooks.remove(slug);
                Cookie cookie = new Cookie(cookieName, String.join("/", cookieBooks));
                cookie.setPath("/");
                response.addCookie(cookie);
                model.addAttribute(attributeName, false);
                booksRatingAndPopularityService.changePopularity(slug, cookieName, false);
            }
        } else {
            model.addAttribute(attributeName, true);
        }
    }
}
