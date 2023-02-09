package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.dto.SearchWordDto;
import com.example.mybookshopapp.data.dto.UserDto;
import com.example.mybookshopapp.data.entity.BookQuery;
import com.example.mybookshopapp.data.entity.book.links.BookCodeType;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.LocaleResolver;

import java.util.ArrayList;
import java.util.List;

public class ModelAttributeController {

    protected final UserProfileService userProfileService;
    protected final BookShopService bookShopService;
    protected final MessageSource messageSource;
    protected final LocaleResolver localeResolver;

    @Autowired
    public ModelAttributeController(UserProfileService userProfileService, BookShopService bookShopService,
                                    MessageSource messageSource, LocaleResolver localeResolver) {
        this.userProfileService = userProfileService;
        this.bookShopService = bookShopService;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    @ModelAttribute("getUser")
    public UserDto getUserDTO() {
        return userProfileService.getCurrentUserDTO();
    }

    @ModelAttribute("isAuthenticatedUser")
    public boolean isAuthenticatedUser() {
        return userProfileService.isAuthenticatedUser();
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("searchResult")
    public List<BookQuery> searchResult() {
        return new ArrayList<>();
    }

    @ModelAttribute(name = "bookCart")
    public List<BookQuery> bookCart() {
        return new ArrayList<>();
    }

    @ModelAttribute("cartSize")
    public int cartSize() {
        return bookShopService.getBooksUser(BookCodeType.CART).size();
    }

    @ModelAttribute("keptSize")
    public int keptSize() {
        return bookShopService.getBooksUser(BookCodeType.KEPT).size();
    }

    @ModelAttribute("paidSize")
    public int paidSize() {
        return bookShopService.getBooksUser(BookCodeType.PAID).size();
    }
}
