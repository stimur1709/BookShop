package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dto.BookStatusRequestDto;
import com.example.mybookshopapp.dto.ResponseResultDto;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.links.BookCodeType;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public ResponseResultDto changeBookStatus(BookStatusRequestDto dto) {
        if (userProfileService.isAuthenticatedUser()) {
            return book2UserTypeService.changeBookStatus(dto);
        }
        return cookieBooksService.changeBookStatus(dto);
    }

    public BookCodeType getBookStatus(Book book) {
        if (userProfileService.isAuthenticatedUser()) {
            return book2UserTypeService.getBookStatus(book);
        }
        return cookieBooksService.getBookStatus(book.getSlug());
    }

    public List<Book> getBooksUser(String cookie, BookCodeType status) {
        if (userProfileService.isAuthenticatedUser()) {
            return book2UserTypeService.getBooksUser(status);
        }
        return cookieBooksService.getBooksFromCookie(cookie);
    }

}
