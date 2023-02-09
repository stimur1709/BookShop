package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.BookStatusRequestDto;
import com.example.mybookshopapp.data.dto.ResponseResultDto;
import com.example.mybookshopapp.data.entity.BookQuery;
import com.example.mybookshopapp.data.entity.book.links.BookCodeType;
import com.example.mybookshopapp.repository.BookQueryRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookShopService {

    private final CookieBooksService cookieBooksService;
    private final UserProfileService userProfileService;
    private final Book2UserTypeService book2UserTypeService;
    private final BookQueryRepository bookQueryRepository;

    @Autowired
    public BookShopService(CookieBooksService cookieBooksService, UserProfileService userProfileService,
                           Book2UserTypeService book2UserTypeService, BookQueryRepository bookQueryRepository) {
        this.cookieBooksService = cookieBooksService;
        this.userProfileService = userProfileService;
        this.book2UserTypeService = book2UserTypeService;
        this.bookQueryRepository = bookQueryRepository;
    }

    public ResponseResultDto changeBookStatus(BookStatusRequestDto dto) {
        if (userProfileService.isAuthenticatedUser()) {
            return book2UserTypeService.changeBookStatus(dto);
        }
        return cookieBooksService.changeBookStatus(dto);
    }

    public List<BookQuery> getBooksUser(BookCodeType status) {
        return bookQueryRepository.getBooksUser(userProfileService.getUserId(), status.name());
    }

}
