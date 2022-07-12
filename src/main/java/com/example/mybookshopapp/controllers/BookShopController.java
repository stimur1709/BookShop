package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Controller
@RequestMapping("/books")
public class BookShopController {

    private final BookService bookService;

    @Autowired
    public BookShopController(BookService bookService) {
        this.bookService = bookService;
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
        } else {
            model.addAttribute("isCartEmpty", false);
            model.addAttribute("bookCart", getBooksFromCookie(cartContents));
        }
        return "cart";
    }

    @GetMapping("/postponed")
    public String postponedPage(@CookieValue(value = "keptContents", required = false) String keptContents,
                                Model model) {
        if (keptContents == null || keptContents.equals("")) {
            model.addAttribute("isKeptEmpty", true);
        } else {
            model.addAttribute("isKeptEmpty", false);
            model.addAttribute("bookKept", getBooksFromCookie(keptContents));
        }
        return "postponed";
    }

    private List<BookEntity> getBooksFromCookie(String contents) {
        contents = contents.startsWith("/") ? contents.substring(1) : contents;
        contents = contents.endsWith("/") ? contents.substring(0, contents.length() - 1)
                : contents;
        String[] cookieSlugs = contents.split("/");
        return bookService.findBookEntitiesBySlugIn(cookieSlugs);
    }

    @PostMapping("/changeBookStatus/{slug}")
    public String handlerChangeBookStatus(@PathVariable("slug") String slug,
                                          @RequestParam(value = "status", required = false) String status,
                                          @CookieValue(name = "keptContents", required = false) String keptContents,
                                          @CookieValue(name = "cartContents", required = false) String cartContents,
                                          HttpServletResponse response, Model model) {
        switch (status) {
            case ("KEPT"):
                createCookie(keptContents, slug, response, "keptContents", model, "isKeptEmpty");
                break;
            case ("CART"):
                createCookie(cartContents, slug, response, "cartContents", model, "isCartEmpty");
                break;
        }
        return "redirect:/books/" + slug;
    }

    private void createCookie(String contents, String slug, HttpServletResponse response,
                              String cookieName, Model model, String attributeName) {
        if (contents == null || contents.equals("")) {
            Cookie cookie = new Cookie(cookieName, slug);
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute(attributeName, false);
        } else if (!contents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(contents).add(slug);
            Cookie cookie = new Cookie(cookieName, stringJoiner.toString());
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute(attributeName, false);
        }
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handlerRemoveBookFromCartRequest(@PathVariable("slug") String slug,
                                                   @CookieValue(name = "cartContents", required = false) String cartContents,
                                                   HttpServletResponse response, Model model) {
        removeCookie(cartContents, slug, response, model, "cartContents", "isCartEmpty");
        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/kept/remove/{slug}")
    public String handlerRemoveBookFromKeptRequest(@PathVariable("slug") String slug,
                                                   @CookieValue(name = "keptContents", required = false) String keptContents,
                                                   HttpServletResponse response, Model model) {
        removeCookie(keptContents, slug, response, model, "keptContents", "isKeptEmpty");
        return "redirect:/books/postponed";
    }

    private void removeCookie(String cookieContent, String slug, HttpServletResponse response, Model model,
                              String cookieName, String attributeName) {
        if (cookieContent != null && !cookieContent.equals("")) {
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cookieContent.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie(cookieName, String.join("/", cookieBooks));
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute(attributeName, false);
        } else {
            model.addAttribute(attributeName, true);
        }
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
