package com.example.mybookshopapp.controllers.view;

import com.example.mybookshopapp.data.dto.SearchWordDto;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

public abstract class ViewControllerImpl {

    private final UserProfileService userProfileService;
    private final HttpServletRequest request;

    protected ViewControllerImpl(UserProfileService userProfileService, HttpServletRequest request) {
        this.userProfileService = userProfileService;
        this.request = request;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("isAuthenticatedUser")
    public boolean isAuthenticatedUser() {
        return userProfileService.isAuthenticatedUser();
    }

    public String getUrl() {
        return request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);
    }

}
