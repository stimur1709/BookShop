package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.dto.SearchWordDto;
import com.example.mybookshopapp.data.entity.BooksQuery;
import com.example.mybookshopapp.errors.EmptySearchException;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.BooksServiceImpl;
import com.example.mybookshopapp.service.userService.UserProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;

@Controller
@Tag(name = "Главная страница", description = "Выводит на странице список книг и облако тэгов")
public class MainPageController extends ModelAttributeController {

    private final BooksServiceImpl booksServiceImpl;
    private final HttpServletRequest request;

    @Autowired
    public MainPageController(BooksServiceImpl booksServiceImpl, UserProfileService userProfileService,
                              BookShopService bookShopService, MessageSource messageSource, LocaleResolver localeResolver,
                              HttpServletRequest request) {
        super(userProfileService, bookShopService, messageSource, localeResolver, request);
        this.booksServiceImpl = booksServiceImpl;
        this.request = request;
    }

    @GetMapping(value = {"/search/{searchWord}", "/search"})
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                  Model model) throws EmptySearchException {
        if (searchWordDto != null) {
            Page<BooksQuery> books = booksServiceImpl.getPageOfSearchResultBooks(searchWordDto.getExample(), PageRequest.of(0, 20));
            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("books", books);
            return "search/index";
        } else {
            String message = messageSource.getMessage("message.searchRequest", null, localeResolver.resolveLocale(request));
            throw new EmptySearchException(message);
        }
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/faq")
    public String faqPage() {
        return "faq";
    }

}