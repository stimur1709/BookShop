package com.example.mybookshopapp.controllers.view;

import com.example.mybookshopapp.data.dto.book.BooksFDto;
import com.example.mybookshopapp.data.outher.Balance;
import com.example.mybookshopapp.service.BookServiceImpl;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.PaymentService;
import com.example.mybookshopapp.service.user.UserProfileService;
import com.example.mybookshopapp.util.MessageLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookShopController extends ViewControllerImpl {

    private final PaymentService paymentService;
    private final BookServiceImpl bookService;

    @Autowired
    protected BookShopController(UserProfileService userProfileService, HttpServletRequest request,
                                 PaymentService paymentService, BookServiceImpl bookService,
                                 BookShopService bookShopService, MessageLocale messageLocale) {
        super(userProfileService, request, bookShopService, messageLocale);
        this.paymentService = paymentService;
        this.bookService = bookService;
    }

    @GetMapping(value = {"/cart", "/postponed", "/my", "/myarchive"})
    public String cartPage(@RequestParam(value = "uuid", required = false) String uuid, Model model) {
        if (uuid != null) {
            paymentService.getStatusPaymentByUCodePaymentEx(uuid);
        }

        List<BooksFDto> bookList = bookService.getBookUser();

        if (bookList == null || bookList.isEmpty()) {
            model.addAttribute("emptyList", true);
        } else {
            model.addAttribute("emptyList", false);
            model.addAttribute("books", bookList);
            model.addAttribute("booksSlug", bookList.stream().map(BooksFDto::getSlug).collect(Collectors.toList()));
            model.addAttribute("priceAll", bookList.stream().mapToInt(BooksFDto::discountPrice).sum());
            model.addAttribute("priceAllNoDisc", bookList.stream().mapToInt(BooksFDto::getPrice).sum());
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

}