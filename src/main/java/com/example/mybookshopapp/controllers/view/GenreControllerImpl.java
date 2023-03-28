package com.example.mybookshopapp.controllers.view;

import com.example.mybookshopapp.data.dto.BookQuery;
import com.example.mybookshopapp.data.dto.BooksFDto;
import com.example.mybookshopapp.data.dto.GenreDto;
import com.example.mybookshopapp.data.dto.Query;
import com.example.mybookshopapp.data.entity.news.Genre;
import com.example.mybookshopapp.service.news.BookService;
import com.example.mybookshopapp.service.news.GenreServiceImpl;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/genres")
public class GenreControllerImpl extends ViewControllerImpl<Genre, Query, GenreDto, GenreServiceImpl> {

    private final BookService bookService;

    @Autowired
    protected GenreControllerImpl(UserProfileService userProfileService, GenreServiceImpl service, BookService bookService) {
        super("genres/index", userProfileService, service);
        this.bookService = bookService;
    }

    @Override
    public String getPage(Model model) {
        model.addAttribute("genres", service.getParentGenreList());
        return super.getPage(model);
    }

    @GetMapping("/{slug}")
    public String getPage(@PathVariable("slug") String slug, Model model) {
        model.addAttribute("content", service.getContent(slug));
        Page<BooksFDto> books = bookService.getContents(new BookQuery(0, 20, "genre", slug));
        model.addAttribute("booksGenre", books.getContent());
        model.addAttribute("show", books.getTotalPages() > 1);
        model.addAttribute("totalPages", books.getTotalPages());
        return "genres/slug";
    }
}