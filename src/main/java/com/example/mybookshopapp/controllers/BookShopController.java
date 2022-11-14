package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BookStatusRequestDto;
import com.example.mybookshopapp.dto.ResponseResultDto;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.links.BookCodeType;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookShopController extends ModelAttributeController {


    @Autowired
    public BookShopController(BookShopService bookShopService,
                              UserProfileService userProfileService) {
        super(userProfileService, bookShopService);
    }

    @GetMapping(value = {"/cart", "/postponed"})
    public String cartPage(@CookieValue(name = "cartContent", required = false) String cartContent,
                           @CookieValue(name = "keptContent", required = false) String keptContent,
                           Model model, HttpServletRequest request) {

        List<Book> bookList = getBooksUser(cartContent, keptContent, request);

        if (bookList == null || bookList.isEmpty()) {
            model.addAttribute("emptyList", true);
        } else {
            model.addAttribute("emptyList", false);
            model.addAttribute("books", bookList);
            model.addAttribute("booksSlug", bookList.stream().map(Book::getSlug).collect(Collectors.toList()));
            model.addAttribute("priceAll", bookList.stream().mapToInt(Book::discountPrice).sum());
            model.addAttribute("priceAllNoDisc", bookList.stream().mapToInt(Book::getPrice).sum());
        }

        return getUrl(request);
    }

    @GetMapping(value = {"/api/size/cart", "/api/size/kept"})
    @ResponseBody
    public int getSize(@CookieValue(name = "cartContent", required = false) String cartContent,
                       @CookieValue(name = "keptContent", required = false) String keptContent,
                       HttpServletRequest request) {
        return getBooksUser(cartContent, keptContent, request).size();
    }

    @PostMapping("/books/changeBookStatus")
    @ResponseBody
    public ResponseResultDto handlerChangeBookStatus(@RequestBody BookStatusRequestDto dto,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {
        return getBookShopService().changeBookStatus(response, request, dto);
    }

    private List<Book> getBooksUser(@CookieValue(name = "cartContent", required = false) String cartContent,
                                    @CookieValue(name = "keptContent", required = false) String keptContent,
                                    HttpServletRequest request) {
        String url = getUrl(request);
        BookCodeType status = url.equals("cart") ? BookCodeType.CART : BookCodeType.KEPT;

        return status.equals(BookCodeType.CART)
                ? getBookShopService().getBooksUser(cartContent, status)
                : getBookShopService().getBooksUser(keptContent, status);
    }

    private String getUrl(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);
    }
}