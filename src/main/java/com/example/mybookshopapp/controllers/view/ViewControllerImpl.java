package com.example.mybookshopapp.controllers.view;

import com.example.mybookshopapp.data.dto.UserDto;
import com.example.mybookshopapp.data.dto.book.BooksFDto;
import com.example.mybookshopapp.data.outher.SearchWordDto;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import com.example.mybookshopapp.util.MessageLocale;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public abstract class ViewControllerImpl {

    protected final UserProfileService userProfileService;
    private final HttpServletRequest request;
    protected final BookShopService bookShopService;
    protected final MessageLocale messageLocale;

    protected ViewControllerImpl(UserProfileService userProfileService, HttpServletRequest request,
                                 BookShopService bookShopService, MessageLocale messageLocale) {
        this.userProfileService = userProfileService;
        this.request = request;
        this.bookShopService = bookShopService;
        this.messageLocale = messageLocale;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("isAuthenticatedUser")
    public boolean isAuthenticatedUser() {
        return userProfileService.isAuthenticatedUser();
    }

    @ModelAttribute("getUser")
    public UserDto getUserDTO() {
        return userProfileService.getCurrentUserDTO();
    }

    @ModelAttribute("searchResult")
    public List<BooksFDto> searchResult() {
        return new ArrayList<>();
    }

    @ModelAttribute("cartSize")
    public long cartSize() {
        return bookShopService.getCountBooksForUser(List.of(2));
    }

    @ModelAttribute("keptSize")
    public long keptSize() {
        return bookShopService.getCountBooksForUser(List.of(1));
    }

    @ModelAttribute("paidSize")
    public long paidSize() {
        return bookShopService.getCountBooksForUser(List.of(3, 4));
    }

    public String getUrl() {
        return request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);
    }
}
