package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksStatusRequestDto;
import com.example.mybookshopapp.dto.ResponseResultDto;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.BooksRatingAndPopularityService;
import com.example.mybookshopapp.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookShopController extends ModelAttributeController {

    private final BookService bookService;
    private final BooksRatingAndPopularityService booksRatingAndPopularityService;
    private final BookShopService bookShopService;

    @Autowired
    public BookShopController(BookService bookService,
                              BooksRatingAndPopularityService booksRatingAndPopularityService,
                              BookShopService bookShopService, UserProfileService userProfileService) {
        super(userProfileService);
        this.bookService = bookService;
        this.booksRatingAndPopularityService = booksRatingAndPopularityService;
        this.bookShopService = bookShopService;
    }

    @GetMapping("/cart")
    public String handlerCartRequest(Model model) {
        List<String> cartContents = bookShopService.getBooksFromCookie("cartContents");
        if (cartContents == null || cartContents.isEmpty()) {
            model.addAttribute("isCartEmpty", true);
            model.addAttribute("isCartSize", 0);
        } else {
            List<Book> bookList = bookService.getBooksFromCookie(cartContents.toString());
            model.addAttribute("isCartEmpty", false);
            model.addAttribute("bookCart", bookList);
            model.addAttribute("isCartSize", bookList.size());
        }
        return "cart";
    }

    @GetMapping("/postponed")
    public String postponedPage(Model model) {
        List<String> keptContents = bookShopService.getBooksFromCookie("keptContents");

        if (keptContents == null || keptContents.isEmpty()) {
            model.addAttribute("isKeptEmpty", true);
            model.addAttribute("isKeptSize", 0);
        } else {
            List<Book> bookList = bookService.getBooksFromCookie(keptContents.toString());
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

        for (Cookie cookie : Arrays.stream(request.getCookies()).collect(Collectors.toList())) {
            System.out.println(cookie.getName() + cookie.getValue());
        }


//        switch (booksStatusRequestDto.getStatus()) {
//            case KEPT:
//                handlerRemoveBookFromCartRequest(booksStatusRequestDto.getBooksIds(), cartContents, response, model);
//                bookShopService.createCookie(keptContents, booksStatusRequestDto.getBooksIds(), response, "keptContents", model, "isKeptEmpty");
//                booksRatingAndPopularityService.changePopularity(booksStatusRequestDto.getBooksIds(), "keptContents", true);
//                break;
//            case CART:
//                handlerRemoveBookFromKeptRequest(booksStatusRequestDto.getBooksIds(), keptContents, response, model);
//                bookShopService.createCookie(cartContents, booksStatusRequestDto.getBooksIds(), response, "cartContents", model, "isCartEmpty");
//                booksRatingAndPopularityService.changePopularity(booksStatusRequestDto.getBooksIds(), "cartContents", true);
//                break;
//            case UNLINK:
//                break;
//            default:
//                return new ResponseResultDto(false, "Нельзя отложить купленную книгу");
//        }
        return bookShopService.changeBookStatus(response, request, dto);
    }

//    @PostMapping("/books/changeBookStatus/cart/remove/{slug}")
//    public void handlerRemoveBookFromCartRequest(@PathVariable("slug") String slug,
//                                                 @CookieValue(name = "cartContents", required = false) String cartContents,
//                                                 HttpServletResponse response, Model model) {
//        bookShopService.removeCookie(cartContents, slug, response, model, "cartContents", "isCartEmpty");
//    }
//
//    @PostMapping("/books/changeBookStatus/kept/remove/{slug}")
//    public void handlerRemoveBookFromKeptRequest(@PathVariable("slug") String slug,
//                                                 @CookieValue(name = "keptContents", required = false) String keptContents,
//                                                 HttpServletResponse response, Model model) {
//        bookShopService.removeCookie(keptContents, slug, response, model, "keptContents", "isKeptEmpty");
//    }
}