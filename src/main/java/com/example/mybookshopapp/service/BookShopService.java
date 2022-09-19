package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dto.BookStatusRequestDto;
import com.example.mybookshopapp.dto.ResponseResultDto;
import com.example.mybookshopapp.dto.Status;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.links.BookCodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class BookShopService {

    private final CookieService cookieService;
    private final UserProfileService userProfileService;
    private final Book2UserTypeService book2UserTypeService;

    @Autowired
    public BookShopService(CookieService cookieService,
                           UserProfileService userProfileService, Book2UserTypeService book2UserTypeService) {
        this.cookieService = cookieService;
        this.userProfileService = userProfileService;
        this.book2UserTypeService = book2UserTypeService;
    }

    public ResponseResultDto changeBookStatus(HttpServletResponse response, HttpServletRequest request,
                                              BookStatusRequestDto dto) {
        if (userProfileService.isAuthenticatedUser()) {
            return book2UserTypeService.changeBookStatus(dto);
        }

        return cookieService.changeBookStatus(response, request.getCookies(), dto);
    }

    public Status getBookStatus(HttpServletRequest request, String slug) {
        return cookieService.getBookStatus(slug, request.getCookies());
    }

    public List<Book> getBooksUser(String cookie, BookCodeType status) {
        if (userProfileService.isAuthenticatedUser()) {
            return book2UserTypeService.getBooksUser(status);
        }
        return cookieService.getBooksFromCookie(cookie);
    }

}
