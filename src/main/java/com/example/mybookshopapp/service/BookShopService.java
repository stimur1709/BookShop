package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.BookStatusRequestDto;
import com.example.mybookshopapp.data.dto.ResponseResultDto;
import com.example.mybookshopapp.data.entity.BooksQuery;
import com.example.mybookshopapp.data.entity.book.links.BookCodeType;
import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.repository.Book2UserRepository;
import com.example.mybookshopapp.repository.BooksQueryRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookShopService {

    private final UserProfileService userProfileService;
    private final BooksQueryRepository booksQueryRepository;
    private final Book2UserRepository book2UserRepository;

    @Autowired
    public BookShopService(UserProfileService userProfileService, BooksQueryRepository booksQueryRepository, Book2UserRepository book2UserRepository) {
        this.userProfileService = userProfileService;
        this.booksQueryRepository = booksQueryRepository;
        this.book2UserRepository = book2UserRepository;
    }

    public ResponseResultDto changeBookStatus(BookStatusRequestDto dto) {
        String[] slugs = dto.getBooksIds().replace("[", "").replace("]", "").split(", ");
        User user = userProfileService.getCurrentUser();
        for (String slug : slugs) {
            book2UserRepository.updateOrCreateType(slug, user.getId(), getIntStatus(dto.getStatus()));
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

    public List<BooksQuery> getBooksUser(BookCodeType status) {
        return booksQueryRepository.getBooksUser(userProfileService.getUserId(), status.name());
    }

    public long getCountBooksForUser(List<Integer> ids) {
        return booksQueryRepository.getCountBooksForUser(userProfileService.getUserId(), ids);
    }

    @Async
    public void addBooksType(int userOld, int userNew) {
        book2UserRepository.updateUserBooksType(userOld, userNew);
    }
}
