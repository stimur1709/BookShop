package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dto.BooksStatusRequestDto;
import com.example.mybookshopapp.dto.ResponseResultDto;
import com.example.mybookshopapp.dto.Status;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class BookShopService {

    private final CookieService cookieService;
    private final BookRepository bookRepository;
    private final UserProfileService userProfileService;
    private final Book2UserTypeService book2UserTypeService;

    @Autowired
    public BookShopService(CookieService cookieService, BookRepository bookRepository,
                           UserProfileService userProfileService, Book2UserTypeService book2UserTypeService) {
        this.cookieService = cookieService;
        this.bookRepository = bookRepository;
        this.userProfileService = userProfileService;
        this.book2UserTypeService = book2UserTypeService;
    }

    public ResponseResultDto changeBookStatus(HttpServletResponse response, HttpServletRequest request,
                                              BooksStatusRequestDto dto) {
        if (userProfileService.isAuthenticatedUser()) {
            return book2UserTypeService.changeBookStatus(dto);
        }

        return cookieService.changeBookStatus(response, request.getCookies(), dto);
    }

    public Status getBookStatus(HttpServletRequest request, String slug) {
        return cookieService.getBookStatus(slug, request.getCookies());
    }

    public List<Book> getBooksFromCookie(String cookie) {
        if (cookie != null) {
            return bookRepository.findBookEntitiesBySlugIn(cookieService.getBooksFromCookie(cookie));
        }
        return Collections.emptyList();
    }
}
