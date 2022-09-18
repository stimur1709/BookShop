package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksStatusRequestDto;
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

@Controller
public class BookShopController extends ModelAttributeController {

    private final BookShopService bookShopService;

    @Autowired
    public BookShopController(BookShopService bookShopService, UserProfileService userProfileService) {
        super(userProfileService);
        this.bookShopService = bookShopService;
    }

    @GetMapping(value = {"/cart", "/postponed"})
    public String cartPage(@CookieValue(name = "cartContent", required = false) String cartContent,
                           @CookieValue(name = "keptContent", required = false) String keptContents,
                           Model model, HttpServletRequest request) {

        String url = getUrl(request);
        List<Book> bookList = getBooks(cartContent, keptContents, request);

        if (bookList == null || bookList.isEmpty()) {
            model.addAttribute("emptyList", true);
        } else {
            model.addAttribute("empty", false);
            model.addAttribute("books", bookList);
        }

        return url;
    }

    @GetMapping(value = {"/api/cartSize", "/api/keptSize"})
    @ResponseBody
    public int getCartSize(@CookieValue(name = "cartContent", required = false) String cartContents,
                           @CookieValue(name = "keptContent", required = false) String keptContents,
                           HttpServletRequest request) {
        List<Book> bookList = getBooks(cartContents, keptContents, request);
        return bookList.isEmpty() ? 0 : bookList.size();
    }

    @PostMapping("/books/changeBookStatus")
    @ResponseBody
    public ResponseResultDto handlerChangeBookStatus(@RequestBody BooksStatusRequestDto dto,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {
        return bookShopService.changeBookStatus(response, request, dto);
    }

    private String getUrl(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);
    }

    private List<Book> getBooks(@CookieValue(name = "cartContent", required = false) String cartContent,
                                @CookieValue(name = "keptContent", required = false) String keptContents,
                                HttpServletRequest request) {
        BookCodeType status = getUrl(request).equals("cartSize") || getUrl(request).equals("cart")
                ? BookCodeType.CART : BookCodeType.KEPT;
        System.out.println(getUrl(request));
        System.out.println(status + " !!!!!!!");
        return status.equals(BookCodeType.CART)
                ? bookShopService.getBooksUser(cartContent, status) : bookShopService.getBooksUser(keptContents, status);
    }

}