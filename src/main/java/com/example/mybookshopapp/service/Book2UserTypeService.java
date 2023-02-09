package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.BookStatusRequestDto;
import com.example.mybookshopapp.data.dto.ResponseResultDto;
import com.example.mybookshopapp.data.entity.book.Book;
import com.example.mybookshopapp.data.entity.book.links.BookCodeType;
import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.repository.Book2UserRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class Book2UserTypeService {

    private final BookService bookService;
    private final UserProfileService userProfileService;
    private final Book2UserRepository book2UserRepository;
    private final CookieBooksService cookieBooksService;

    @Autowired
    public Book2UserTypeService(BookService bookService, UserProfileService userProfileService,
                                Book2UserRepository book2UserRepository, CookieBooksService cookieBooksService) {
        this.bookService = bookService;
        this.userProfileService = userProfileService;
        this.book2UserRepository = book2UserRepository;
        this.cookieBooksService = cookieBooksService;
    }

    public ResponseResultDto changeBookStatus(BookStatusRequestDto dto) {
        String[] slugs = dto.getBooksIds().replace("[", "").replace("]", "").split(", ");
        User user = userProfileService.getCurrentUser();
        for (String slug : slugs) {
            Book book = bookService.getBookBySlug(slug);
            changeTypeBook2User(book, user, getIntStatus(dto.getStatus()));
        }
        return new ResponseResultDto(true);
    }

    private Integer getIntStatus(BookCodeType status) {
        switch (status) {
            case KEPT:
                return 1;
            case CART:
                return 2;
            case PAID:
                return 3;
            case ARCHIVED:
                return 4;
            default:
                return 5;
        }
    }

    private void changeTypeBook2User(Book book, User user, int typeId) {
        book2UserRepository.updateOrCreateType(book.getId(), user.getId(), typeId);
    }

    public void addBooksTypeUserFromCookie(User user) {
        Map<BookCodeType, List<Book>> books = cookieBooksService.getBooksFromCookies();
        if (!books.isEmpty()) {
            books.forEach((key, value) -> value.forEach(book -> changeTypeBook2User(book, user, getIntStatus(key))));
        }
    }
}