package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.author.Author;
import com.example.mybookshopapp.data.book.BookEntity;
import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.service.AuthorService;
import com.example.mybookshopapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MainPageController {

    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public MainPageController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @ModelAttribute("recommendBooks")
    public List<BookEntity> recommendBooks() {
        return bookService.getPageOfRecommendBooks(0, 6).getContent();
    }

    @GetMapping("/books/recommended")
    @ResponseBody
    public BooksPageDto getBooksPage(@RequestParam("offset") Integer offset,
                                     @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecommendBooks(offset, limit).getContent());
    }

    @ModelAttribute("recentBooks")
    public List<BookEntity> recentBooks() {
        return bookService.getPageOfRecentBooks(0, 6).getContent();
    }

    @GetMapping("/books/resent")
    @ResponseBody
    public BooksPageDto getRecentBooksPage(@RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecentBooks(offset, limit).getContent());
    }

//    @ModelAttribute("popularBooks")
//    public List<BookEntity> popularBooks() {
//        return bookService.getPageOfPopularBooks(0, 6).getContent();
//    }
//
//    @GetMapping("/books/popular")
//    @ResponseBody
//    public BooksPageDto getPopularBooksPage(@RequestParam("offset") Integer offset,
//                                           @RequestParam("limit") Integer limit) {
//        return new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit).getContent());
//    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("searchResult")
    public List<BookEntity> searchResult() {
        return new ArrayList<>();
    }

    @GetMapping("/search/{searchWord}")
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                  Model model) {
        model.addAttribute("searchWordDto", searchWordDto);
        model.addAttribute("searchResult",
                bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), 0, 5).getContent());
        return "search/index";
    }

    @GetMapping("/search/page/{searchWord}")
    @ResponseBody
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false)
                                                  SearchWordDto searchWordDto) {
        return new BooksPageDto(bookService.getPageOfSearchResultBooks(searchWordDto.getExample(),
                offset, limit).getContent());
    }

    @GetMapping("/search")
    public String getSearchPage() {
        return "search/index";
    }

    @ModelAttribute("authorsMap")
    public Map<String, List<Author>> authorsMap() {
        return authorService.getAuthorsMap();
    }

    @GetMapping("/authors")
    public String authorsPage() {
        return "authors/index";
    }

//    @GetMapping("/books/recent")
//    public String recentPage(Model model) {
//        model.addAttribute("bookList", bookService.getBooksData());
//        return "books/recent";
//    }

    @GetMapping("/genres")
    public String genresPage() {
        return "genres/index";
    }

//    @GetMapping("/books/popular")
//    public String popularPage(Model model) {
//        model.addAttribute("bookList", bookService.getBooksData());
//        return "books/popular";
//    }

    @GetMapping("/postponed")
    public String postponedPage() {
        return "postponed";
    }

    @GetMapping("/signin")
    public String signinPage() {
        return "signin";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/cart")
    public String cartPage() {
        return "cart";
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

