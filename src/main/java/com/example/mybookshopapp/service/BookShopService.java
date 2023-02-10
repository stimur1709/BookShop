package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.BookStatusRequestDto;
import com.example.mybookshopapp.data.dto.ResponseResultDto;
import com.example.mybookshopapp.data.entity.BookQuery;
import com.example.mybookshopapp.data.entity.book.Book;
import com.example.mybookshopapp.data.entity.book.links.BookCodeType;
import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.repository.Book2UserRepository;
import com.example.mybookshopapp.repository.BookQueryRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookShopService {

    private final UserProfileService userProfileService;
    private final BookQueryRepository bookQueryRepository;
    private final BookService bookService;
    private final Book2UserRepository book2UserRepository;

    @Autowired
    public BookShopService(UserProfileService userProfileService, BookQueryRepository bookQueryRepository, BookService bookService, Book2UserRepository book2UserRepository) {
        this.userProfileService = userProfileService;
        this.bookQueryRepository = bookQueryRepository;
        this.bookService = bookService;
        this.book2UserRepository = book2UserRepository;
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

    private void changeTypeBook2User(Book book, User user, int typeId) {
        book2UserRepository.updateOrCreateType(book.getId(), user.getId(), typeId);
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

    public List<BookQuery> getBooksUser(BookCodeType status) {
        return bookQueryRepository.getBooksUser(userProfileService.getUserId(), status.name());
    }

    @Async
    public void addBooksType(int userOld, int userNew) {
        book2UserRepository.updateUserBooksType(userOld, userNew);
    }
}
