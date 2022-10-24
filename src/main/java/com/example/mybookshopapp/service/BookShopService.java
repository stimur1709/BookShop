package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dto.BookStatusRequestDto;
import com.example.mybookshopapp.dto.ResponseResultDto;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.links.BookCodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class BookShopService {

    private final CookieBooksService cookieBooksService;
    private final UserProfileService userProfileService;
    private final Book2UserTypeService book2UserTypeService;

    @Autowired
    public BookShopService(CookieBooksService cookieBooksService, UserProfileService userProfileService,
                           Book2UserTypeService book2UserTypeService) {
        this.cookieBooksService = cookieBooksService;
        this.userProfileService = userProfileService;
        this.book2UserTypeService = book2UserTypeService;
    }

    public ResponseResultDto changeBookStatus(HttpServletResponse response, HttpServletRequest request,
                                              BookStatusRequestDto dto) {
        if (userProfileService.isAuthenticatedUser()) {
            return book2UserTypeService.changeBookStatus(dto);
        }

        return cookieBooksService.changeBookStatus(response, request.getCookies(), dto);
    }

    public BookCodeType getBookStatus(HttpServletRequest request, Book book) {
        if (userProfileService.isAuthenticatedUser()) {
            return book2UserTypeService.getBookStatus(book);
        }

        return cookieBooksService.getBookStatus(book.getSlug(), request.getCookies());
    }

    public List<Book> getBooksUser(String cookie, BookCodeType status) {
        if (userProfileService.isAuthenticatedUser()) {
            return book2UserTypeService.getBooksUser(status);
        }
        return cookieBooksService.getBooksFromCookie(cookie);
    }

}
