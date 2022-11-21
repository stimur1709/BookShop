package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BookStatusRequestDto;
import com.example.mybookshopapp.dto.ResponseResultDto;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.links.BookCodeType;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookShopController extends ModelAttributeController {

    private final HttpServletRequest request;

    @Autowired
    public BookShopController(BookShopService bookShopService, UserProfileService userProfileService,
                              MessageSource messageSource, LocaleResolver localeResolver, HttpServletRequest request) {
        super(userProfileService, bookShopService, messageSource, localeResolver);
        this.request = request;
    }

    @GetMapping(value = {"/cart", "/postponed"})
    public String cartPage(@CookieValue(name = "cartContent", required = false) String cartContent,
                           @CookieValue(name = "keptContent", required = false) String keptContent,
                           Model model) {

        List<Book> bookList = getBooksUser(cartContent, keptContent);

        if (bookList == null || bookList.isEmpty()) {
            model.addAttribute("emptyList", true);
        } else {
            model.addAttribute("emptyList", false);
            model.addAttribute("books", bookList);
            model.addAttribute("booksSlug", bookList.stream().map(Book::getSlug).collect(Collectors.toList()));
            model.addAttribute("priceAll", bookList.stream().mapToInt(Book::discountPrice).sum());
            model.addAttribute("priceAllNoDisc", bookList.stream().mapToInt(Book::getPrice).sum());
        }

        return getUrl();
    }

    @GetMapping(value = {"/api/size/cart", "/api/size/kept"})
    @ResponseBody
    public int getSize(@CookieValue(name = "cartContent", required = false) String cartContent,
                       @CookieValue(name = "keptContent", required = false) String keptContent) {
        return getBooksUser(cartContent, keptContent).size();
    }

    @PostMapping("/books/changeBookStatus")
    @ResponseBody
    public ResponseResultDto handlerChangeBookStatus(@RequestBody BookStatusRequestDto dto) {
        return bookShopService.changeBookStatus(dto);
    }

    private List<Book> getBooksUser(@CookieValue(name = "cartContent", required = false) String cartContent,
                                    @CookieValue(name = "keptContent", required = false) String keptContent) {
        String url = getUrl();
        BookCodeType status = url.equals("cart") ? BookCodeType.CART : BookCodeType.KEPT;

        return status.equals(BookCodeType.CART)
                ? bookShopService.getBooksUser(cartContent, status)
                : bookShopService.getBooksUser(keptContent, status);
    }

    private String getUrl() {
        return request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);
    }
}