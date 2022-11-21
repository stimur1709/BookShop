package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.dto.UserDto;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.links.BookCodeType;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.CookieValue;
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
    public List<Book> searchResult() {
        return new ArrayList<>();
    }

    @ModelAttribute(name = "bookCart")
    public List<Book> bookCart() {
        return new ArrayList<>();
    }

    @ModelAttribute("cartSize")
    public int cartSize(@CookieValue(name = "cartContent", required = false) String cartContent) {
        return bookShopService.getBooksUser(cartContent, BookCodeType.CART).size();
    }

    @ModelAttribute("keptSize")
    public int keptSize(@CookieValue(name = "keptContent", required = false) String keptContent) {
        return bookShopService.getBooksUser(keptContent, BookCodeType.KEPT).size();
    }
}
