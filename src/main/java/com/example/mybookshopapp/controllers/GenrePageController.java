package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.GenreDto;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.GenreService;
import com.example.mybookshopapp.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Tag(name = "Страница жанров")
public class GenrePageController extends ModelAttributeController {

    private final GenreService genreService;
    private final BookService bookService;

    @Autowired
    public GenrePageController(GenreService genreService,
                               BookService bookService,
                               UserProfileService userProfileService) {
        super(userProfileService);
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @GetMapping("/genres")
    public String genresPage(Model model) {
        model.addAttribute("getGenreMap", genreService.getGenreMap());
        return "genres/index";
    }

    @GetMapping("/genres/{slug}")
    public String genresSlugPage(@PathVariable("slug") String slug, Model model) {
        GenreDto genre = genreService.getPageBySlug(slug);
        model.addAttribute("genre", genre);
        model.addAttribute("parentGenre", genreService.getPageById(genre.getSlug()));
        model.addAttribute("booksGenre", bookService.getBooksForPageGenre(genre, 0, 20));
        return "genres/slug";
    }

    @GetMapping("/api/books/genre/{slug}")
    @ResponseBody
    @Operation(summary = "Постраничный вывод книг",
            description = "Выводит на странице книги, привязанных к данному жанру")
    public BooksPageDto getBooksPage(@PathVariable(value = "slug") String slug,
                                     @RequestParam("offset") Integer offset,
                                     @RequestParam("limit") Integer limit) {
        GenreDto genre = genreService.getPageBySlug(slug);
        return new BooksPageDto(bookService.getBooksForPageGenre(genre, offset, limit).getContent());
    }
}