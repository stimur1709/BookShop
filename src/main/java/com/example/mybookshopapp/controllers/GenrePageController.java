package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.entity.genre.GenreEntity;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Tag(name = "Страница жанров")
public class GenrePageController {

    private final GenreService genreService;
    private final BookService bookService;

    @Autowired
    public GenrePageController(GenreService genreService, BookService bookService) {
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
        GenreEntity genre = genreService.getPageBySlug(slug);
        model.addAttribute("genre", genre);
        model.addAttribute("parentGenre", genreService.getPageById(genre.getParentId()));
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
        GenreEntity genre = genreService.getPageBySlug(slug);
        return new BooksPageDto(bookService.getBooksForPageGenre(genre, offset, limit).getContent());
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
