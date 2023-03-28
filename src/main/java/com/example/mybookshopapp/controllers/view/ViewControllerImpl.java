package com.example.mybookshopapp.controllers.view;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.dto.Query;
import com.example.mybookshopapp.data.dto.SearchWordDto;
import com.example.mybookshopapp.data.entity.news.Models;
import com.example.mybookshopapp.service.news.ModelService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class ViewControllerImpl<M extends Models, Q extends Query, D extends Dto, S extends ModelService<Q, D>>
        implements ViewController {

    private final String view;
    private final UserProfileService userProfileService;
    protected final S service;

    protected ViewControllerImpl(String view, UserProfileService userProfileService, S service) {
        this.view = view;
        this.userProfileService = userProfileService;
        this.service = service;
    }

    @Override
    public String getPage(Model model) {
        return this.view;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("isAuthenticatedUser")
    public boolean isAuthenticatedUser() {
        return userProfileService.isAuthenticatedUser();
    }


}
