package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.dto.UserDto;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

public class ModelAttributeController {

    protected final UserProfileService userProfileService;

    @Autowired
    public ModelAttributeController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @ModelAttribute("getUser")
    public UserDto getUserDTO() {
        return userProfileService.getCurrentUser();
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
}
