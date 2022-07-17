package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.BooksRatingAndPopularityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookShopController {

    private final BookService bookService;
    private final BooksRatingAndPopularityService booksRatingAndPopularityService;
    private final BookShopService bookShopService;

    @Autowired
    public BookShopController(BookService bookService, BooksRatingAndPopularityService booksRatingAndPopularityService, BookShopService bookShopService) {
        this.bookService = bookService;
        this.booksRatingAndPopularityService = booksRatingAndPopularityService;
        this.bookShopService = bookShopService;
    }

    @ModelAttribute(name = "bookCart")
    public List<BookEntity> bookCart() {
        return new ArrayList<>();
    }

    @GetMapping("/cart")
    public String handlerCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents,
                                     Model model) {
        if (cartContents == null || cartContents.equals("")) {
            model.addAttribute("isCartEmpty", true);
            model.addAttribute("isCartSize", 0);
        } else {
            List<BookEntity> bookList = bookService.getBooksFromCookie(cartContents);
            model.addAttribute("isCartEmpty", false);
            model.addAttribute("bookCart", bookList);
            model.addAttribute("isCartSize", bookList.size());
        }
        return "cart";
    }

    @GetMapping("/postponed")
    public String postponedPage(@CookieValue(value = "keptContents", required = false) String keptContents,
                                Model model) {
        if (keptContents == null || keptContents.equals("")) {
            model.addAttribute("isKeptEmpty", true);
            model.addAttribute("isKeptSize", 0);
        } else {
            List<BookEntity> bookList = bookService.getBooksFromCookie(keptContents);
            model.addAttribute("isKeptEmpty", false);
            model.addAttribute("bookKept", bookService.getBooksFromCookie(keptContents));
            model.addAttribute("isKeptSize", bookList.size());
        }
        return "postponed";
    }

    @PostMapping("/changeBookStatus/{slug}")
    public String handlerChangeBookStatus(@PathVariable("slug") String slug,
                                          @RequestParam(value = "status", required = false) String status,
                                          @CookieValue(name = "keptContents", required = false) String keptContents,
                                          @CookieValue(name = "cartContents", required = false) String cartContents,
                                          HttpServletResponse response, Model model) {
        switch (status) {
            case ("KEPT"):
                handlerRemoveBookFromCartRequest(slug, cartContents, response, model);
                bookShopService.createCookie(keptContents, slug, response, "keptContents", model, "isKeptEmpty");
                booksRatingAndPopularityService.changePopularity(slug, "keptContents", true);
                break;
            case ("CART"):
                handlerRemoveBookFromKeptRequest(slug, keptContents, response, model);
                bookShopService.createCookie(cartContents, slug, response, "cartContents", model, "isCartEmpty");
                booksRatingAndPopularityService.changePopularity(slug, "cartContents", true);
                break;
        }
        return "redirect:/books/" + slug;
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handlerRemoveBookFromCartRequest(@PathVariable("slug") String slug,
                                                   @CookieValue(name = "cartContents", required = false) String cartContents,
                                                   HttpServletResponse response, Model model) {
        bookShopService.removeCookie(cartContents, slug, response, model, "cartContents", "isCartEmpty");
        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/kept/remove/{slug}")
    public String handlerRemoveBookFromKeptRequest(@PathVariable("slug") String slug,
                                                   @CookieValue(name = "keptContents", required = false) String keptContents,
                                                   HttpServletResponse response, Model model) {
        bookShopService.removeCookie(keptContents, slug, response, model, "keptContents", "isKeptEmpty");
        return "redirect:/books/postponed";
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("searchResult")
    public List<BookEntity> searchResult() {
        return new ArrayList<>();
    }
}