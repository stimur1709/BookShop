package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.errors.EmptySearchException;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.GenreService;
import com.example.mybookshopapp.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Tag(name = "Главная страница", description = "Выводит на странице список книг и облако тэгов")
public class MainPageController {

    private final BookService bookService;
    private final TagService tagService;
    private final GenreService genreService;

    @Autowired
    public MainPageController(BookService bookService, TagService tagService, GenreService genreService) {
        this.bookService = bookService;
        this.tagService = tagService;
        this.genreService = genreService;
    }

    @GetMapping("/")
    public String mainPage(Model model) {
        genreService.addAmount();
        model.addAttribute("recommendBooks", bookService.getPageOfRecommendBooks(0, 6).getContent());
        model.addAttribute("recentBooks", bookService.getPageOfRecentBooks(0, 6).getContent());
        model.addAttribute("popularBooks", bookService.getPageOfPopularBooks(0, 6).getContent());
        model.addAttribute("tagsBooks", tagService.getPageOfTagsBooks());
        model.addAttribute("sizeBooks", bookService.getNumbersOffAllBooks());
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
            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("searchResult",
                    bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), 0, 5).getContent());
            return "search/index";
        } else {
            throw new EmptySearchException("Введите название книги");
        }
    }

    @GetMapping("/search/page/{searchWord}")
    @ResponseBody
    @Operation(summary = "Поиск книг")
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false)
                                                  SearchWordDto searchWordDto) {
        return new BooksPageDto(bookService.getPageOfSearchResultBooks(searchWordDto.getExample(),
                offset, limit).getContent());
    }

    @GetMapping("/signin")
    public String signinPage() {
        return "signin";
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

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("searchResult")
    public List<BookEntity> searchResult() {
        return new ArrayList<>();
    }
}