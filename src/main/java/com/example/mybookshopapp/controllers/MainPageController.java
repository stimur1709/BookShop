package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.dto.BooksPageDto;
import com.example.mybookshopapp.data.dto.SearchWordDto;
import com.example.mybookshopapp.data.entity.BooksQuery;
import com.example.mybookshopapp.errors.EmptySearchException;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.TagService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;

@Controller
@Tag(name = "Главная страница", description = "Выводит на странице список книг и облако тэгов")
public class MainPageController extends ModelAttributeController {

    private final BookService bookService;
    private final TagService tagService;
    private final HttpServletRequest request;

    @Autowired
    public MainPageController(BookService bookService, TagService tagService, UserProfileService userProfileService,
                              BookShopService bookShopService, MessageSource messageSource, LocaleResolver localeResolver,
                              HttpServletRequest request) {
        super(userProfileService, bookShopService, messageSource, localeResolver, request);
        this.bookService = bookService;
        this.tagService = tagService;
        this.request = request;
    }

    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("recommendBooks", bookService.getPageBooks(0, 6, "rate"));
        model.addAttribute("recentBooks", bookService.getPageBooks(0, 6, "pub_date"));
        model.addAttribute("popularBooks", bookService.getPageBooks(0, 6, "popularity"));
        model.addAttribute("recentlyViewed", bookService.getPageBooks(0, 6, "viewed"));
        model.addAttribute("tagsBooks", tagService.getPageOfTagsBooks());
        return "index";
    }

    @GetMapping("/api/books")
    @ResponseBody
    @Operation(summary = "Постраничный вывод книг")
    public BooksPageDto getBooksPage(@RequestParam("offset") Integer offset,
                                     @RequestParam("limit") Integer limit,
                                     @RequestParam("properties") String properties) {
        return new BooksPageDto(bookService.getPageBooks(offset, limit, properties).getContent());
    }

    @GetMapping("/api/main_page/books/recent")
    @ResponseBody
    @Operation(summary = "Постраничный вывод новых книг")
    public BooksPageDto getRecentBooksPage(@RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageBooks(offset, limit, "pub_date").getContent());
    }

    @GetMapping(value = {"/search/{searchWord}", "/search"})
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                  Model model) throws EmptySearchException {
        if (searchWordDto != null) {
            Page<BooksQuery> books = bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), 0, 20);
            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("books", books);
            return "search/index";
        } else {
            String message = messageSource.getMessage("message.reviewEmpty", null, localeResolver.resolveLocale(request));
            throw new EmptySearchException(message);
        }
    }

    @GetMapping("/search/page/{searchWord}")
    @ResponseBody
    @Operation(summary = "Поиск книг")
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false)
                                          SearchWordDto searchWordDto) {
        Page<BooksQuery> page = bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), offset, limit);
        return new BooksPageDto(page.getContent(), (int) page.getTotalElements());
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