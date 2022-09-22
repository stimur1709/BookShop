package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BookStatusRequestDto;
import com.example.mybookshopapp.dto.ResponseResultDto;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.links.BookCodeType;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.UserProfileService;
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

        String url = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);
        BookCodeType status = url.equals("cart") ? BookCodeType.CART : BookCodeType.KEPT;

        List<Book> bookList = status.equals(BookCodeType.CART)
                ? getBookShopService().getBooksUser(cartContent, status)
                : getBookShopService().getBooksUser(keptContent, status);

        if (bookList == null || bookList.isEmpty()) {
            model.addAttribute("emptyList", true);
        } else {
            model.addAttribute("emptyList", false);
            model.addAttribute("books", bookList);
            model.addAttribute("booksSlug", bookList.stream().map(Book::getSlug).collect(Collectors.toList()));
            model.addAttribute("priceAll", bookList.stream().mapToInt(Book::discountPrice).sum());
            model.addAttribute("priceAllNoDisc", bookList.stream().mapToInt(Book::getPrice).sum());
        }

        return url;
    }

    @PostMapping("/books/changeBookStatus")
    @ResponseBody
    public ResponseResultDto handlerChangeBookStatus(@RequestBody BookStatusRequestDto dto,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {
        System.out.println(dto.getBooksIds());
        return getBookShopService().changeBookStatus(response, request, dto);
    }
}