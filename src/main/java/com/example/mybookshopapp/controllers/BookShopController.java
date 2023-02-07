package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.dto.BookStatusRequestDto;
import com.example.mybookshopapp.data.dto.ResponseResultDto;
import com.example.mybookshopapp.data.entity.book.Book;
import com.example.mybookshopapp.data.entity.book.links.BookCodeType;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.PaymentService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookShopController extends ModelAttributeController {

    private final HttpServletRequest request;
    private final PaymentService paymentService;

    @Autowired
    public BookShopController(BookShopService bookShopService, UserProfileService userProfileService,
                              MessageSource messageSource, LocaleResolver localeResolver, HttpServletRequest request, PaymentService paymentService) {
        super(userProfileService, bookShopService, messageSource, localeResolver);
        this.request = request;
        this.paymentService = paymentService;
    }

    @GetMapping(value = {"/cart", "/postponed", "/my", "/myarchive"})
    public String cartPage(@RequestParam(value = "uuid", required = false) String uuid, Model model) {
        if (uuid != null) {
            paymentService.getStatusPaymentByUCodePaymentEx(uuid);
        }

        List<Book> bookList = getBooksUser();

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

    @GetMapping("/order/{amount}")
    public RedirectView profilePage(@PathVariable("amount") int amount,
                                    @RequestParam(value = "books", required = false) List<String> books) {
        return new RedirectView(paymentService.getPaymentUrl(String.valueOf(amount), "Оплата книг", books));
    }

    @GetMapping(value = {"/api/size/cart", "/api/size/kept"})
    @ResponseBody
    public int getSize() {
        return getBooksUser().size();
    }

    @PostMapping("/books/changeBookStatus")
    @ResponseBody
    public ResponseResultDto handlerChangeBookStatus(@RequestBody BookStatusRequestDto dto) {
        return bookShopService.changeBookStatus(dto);
    }

    private List<Book> getBooksUser() {
        String url = getUrl();
        BookCodeType status = null;
        switch (url) {
            case "cart":
                status = BookCodeType.CART;
                break;
            case "my":
                status = BookCodeType.PAID;
                break;
            case "postponed":
                status = BookCodeType.KEPT;
                break;
            case "myarchive":
                status = BookCodeType.ARCHIVED;
                break;
        }
        return bookShopService.getBooksUser(status);
    }

    private String getUrl() {
        return request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);
    }
}