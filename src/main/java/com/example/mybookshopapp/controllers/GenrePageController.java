package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.dto.BooksPageDto;
import com.example.mybookshopapp.data.entity.BooksQuery;
import com.example.mybookshopapp.data.entity.genre.Genre;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.GenreService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;

@Controller
@Tag(name = "Страница жанров")
public class GenrePageController extends ModelAttributeController {

    private final GenreService genreService;
    private final BookService bookService;

    @Autowired
    public GenrePageController(GenreService genreService, BookService bookService,
                               UserProfileService userProfileService, BookShopService bookShopService,
                               MessageSource messageSource, LocaleResolver localeResolver, HttpServletRequest request) {
        super(userProfileService, bookShopService, messageSource, localeResolver, request);
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @GetMapping("/genres")
    public String genresPage(Model model) {
        model.addAttribute("genres", genreService.getParentGenreList());
        return "genres/index";
    }

    @GetMapping("/genres/{slug}")
    public String genresSlugPage(@PathVariable("slug") String slug, Model model) {
        Genre genre = genreService.getPageBySlug(slug);
        Page<BooksQuery> books = bookService.getBooksForPageGenre(genre, 0, 20);
        model.addAttribute("genre", genre);
        model.addAttribute("booksGenre", books.getContent());
        model.addAttribute("show", books.getTotalPages() > 1);
        model.addAttribute("totalPages", books.getTotalPages());
        return "genres/slug";
    }

    @GetMapping("/api/books/genre/{slug}")
    @ResponseBody
    @Operation(summary = "Постраничный вывод книг",
            description = "Выводит на странице книги, привязанных к данному жанру")
    public BooksPageDto getBooksPage(@PathVariable(value = "slug") String slug,
                                     @RequestParam("offset") Integer offset,
                                     @RequestParam("limit") Integer limit) {
        Genre genre = genreService.getPageBySlug(slug);
        return new BooksPageDto(bookService.getBooksForPageGenre(genre, offset, limit).getContent());
    }
}