package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.errors.EmptySearchException;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@Tag(name = "Главная страница", description = "Выводит на странице список книг и облако тэгов")
public class MainPageController extends ModelAttributeController {

    protected final BookService bookService;
    protected final TagService tagService;
    protected final GenreService genreService;

    @Autowired
    public MainPageController(BookService bookService, TagService tagService,
                              GenreService genreService, UserProfileService userProfileService,
                              BookShopService bookShopService,
                              BlacklistService blacklistService) {
        super(userProfileService, bookShopService);
        this.bookService = bookService;
        this.tagService = tagService;
        this.genreService = genreService;
    }

    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("recommendBooks", bookService.getPageOfRecommendBooks(0, 6).getContent());
        model.addAttribute("recentBooks", bookService.getPageOfRecentBooks(0, 6).getContent());
        model.addAttribute("popularBooks", bookService.getPageOfPopularBooks(0, 6).getContent());
        model.addAttribute("tagsBooks", tagService.getPageOfTagsBooks());
        model.addAttribute("sizeBooks", bookService.getNumbersOffAllBooks());
        model.addAttribute("isAuthenticatedUser", getUserProfileService().isAuthenticatedUser());
        return "index";
    }

    @GetMapping("/api/books/recommended")
    @ResponseBody
    @Operation(summary = "Постраничный вывод рекомендуемых книг")
    public BooksPageDto getBooksPage(@RequestParam("offset") Integer offset,
                                     @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecommendBooks(offset, limit).getContent());
    }

    @GetMapping("/api/main_page/books/recent")
    @ResponseBody
    @Operation(summary = "Постраничный вывод новых книг")
    public BooksPageDto getRecentBooksPage(@RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecentBooks(offset, limit).getContent());
    }

    @GetMapping(value = {"/search/{searchWord}", "/search"})
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                  Model model) throws EmptySearchException {
        if (searchWordDto != null) {
            Page<Book> books = bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), 0, 20);
            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("searchResult", books.getContent());
            model.addAttribute("totalElement", books.getTotalElements());
            model.addAttribute("show", books.getTotalPages() != 0);
            model.addAttribute("totalPages", books.getTotalPages());
            return "search/index";
        } else {
            throw new EmptySearchException("Поисковый запрос не задан");
        }
    }

    @GetMapping("/search/page/{searchWord}")
    @ResponseBody
    @Operation(summary = "Поиск книг")
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false)
                                                  SearchWordDto searchWordDto) {
        return new BooksPageDto(bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), offset, limit).getContent());
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/contacts")
    public String contactsPage() {
        return "contacts";
    }

    @GetMapping("/documents")
    public String documentsPage() {
        return "documents/index";
    }

    @GetMapping("/faq")
    public String faqPage() {
        return "faq";
    }
}