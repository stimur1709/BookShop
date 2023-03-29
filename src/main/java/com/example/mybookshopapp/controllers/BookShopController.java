package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.dto.Balance;
import com.example.mybookshopapp.data.dto.BookStatusRequestDto;
import com.example.mybookshopapp.data.dto.ResponseResultDto;
import com.example.mybookshopapp.data.entity.book.links.BookCodeType;
import com.example.mybookshopapp.data.entity.news.BooksF;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.PaymentService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookShopController extends ModelAttributeController {

    private final PaymentService paymentService;

    @Autowired
    public BookShopController(BookShopService bookShopService, UserProfileService userProfileService,
                              MessageSource messageSource, LocaleResolver localeResolver, HttpServletRequest request, PaymentService paymentService) {
        super(userProfileService, bookShopService, messageSource, localeResolver, request);
        this.paymentService = paymentService;
    }

    @GetMapping(value = {"/cart", "/postponed", "/my", "/myarchive"})
    public String cartPage(@RequestParam(value = "uuid", required = false) String uuid, Model model) {
        if (uuid != null) {
            paymentService.getStatusPaymentByUCodePaymentEx(uuid);
        }

        List<BooksF> bookList = getBooksUser();

        if (bookList == null || bookList.isEmpty()) {
            model.addAttribute("emptyList", true);
        } else {
            model.addAttribute("emptyList", false);
            model.addAttribute("books", bookList);
            model.addAttribute("booksSlug", bookList.stream().map(BooksF::getSlug).collect(Collectors.toList()));
            model.addAttribute("priceAll", bookList.stream().mapToInt(BooksF::discountPrice).sum());
            model.addAttribute("priceAllNoDisc", bookList.stream().mapToInt(BooksF::getPrice).sum());
        }

        return getUrl();
    }

    @GetMapping("/order/{amount}")
    public RedirectView buyBooks(@PathVariable("amount") int amount,
                                 @RequestParam(value = "books", required = false) List<String> books) {
        return paymentService.buyBooks(amount, books);
    }

    @PostMapping("/payment")
    public RedirectView profilePage(@Valid Balance balance, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new RedirectView("profile/#topup");
        }

        return new RedirectView(paymentService.getPaymentUrl(String.valueOf(balance.getSum()), "Пополнение счета"));
    }

    @GetMapping(value = {"/api/size/cart", "/api/size/postponed"})
    @ResponseBody
    public int getSize() {
        return getBooksUser().size();
    }

    @PostMapping("/books/changeBookStatus")
    @ResponseBody
    public ResponseResultDto handlerChangeBookStatus(@RequestBody BookStatusRequestDto dto) {
        return bookShopService.changeBookStatus(dto);
    }

    private List<BooksF> getBooksUser() {
        String url = getUrl();
        BookCodeType status = BookCodeType.UNLINK;
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

}