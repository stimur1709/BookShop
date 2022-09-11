package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksStatusRequestDto;
import com.example.mybookshopapp.dto.ResponseResultDto;
import com.example.mybookshopapp.model.book.Book;
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

    @GetMapping("/cart")
    public String handlerCartRequest(@CookieValue(name = "cartContent", required = false) String cartContent,
                                     Model model) {
        List<Book> bookList = bookShopService.getBooksFromCookie(cartContent);

        if (bookList == null || bookList.isEmpty()) {
            model.addAttribute("isCartEmpty", true);
            model.addAttribute("isCartSize", 0);
        } else {
            model.addAttribute("isCartEmpty", false);
            model.addAttribute("bookCart", bookList);
            model.addAttribute("isCartSize", bookList.size());
        }
        return "cart";
    }

    @GetMapping("/postponed")
    public String postponedPage(@CookieValue(name = "keptContent", required = false) String keptContents,
                                Model model) {
        List<Book> bookList = bookShopService.getBooksFromCookie(keptContents);

        if (bookList == null || bookList.isEmpty()) {
            model.addAttribute("isKeptEmpty", true);
            model.addAttribute("isKeptSize", 0);
        } else {
            model.addAttribute("isKeptEmpty", false);
            model.addAttribute("bookKept", bookList);
            model.addAttribute("isKeptSize", bookList.size());
        }
        return "postponed";
    }

    @PostMapping("/books/changeBookStatus")
    @ResponseBody
    public ResponseResultDto handlerChangeBookStatus(@RequestBody BooksStatusRequestDto dto,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {
        return bookShopService.changeBookStatus(response, request, dto);
    }
}