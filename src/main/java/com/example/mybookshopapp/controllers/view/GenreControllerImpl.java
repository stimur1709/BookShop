package com.example.mybookshopapp.controllers.view;

import com.example.mybookshopapp.data.dto.book.BooksFDto;
import com.example.mybookshopapp.data.query.BookQuery;
import com.example.mybookshopapp.service.BookServiceImpl;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.GenreServiceImpl;
import com.example.mybookshopapp.service.userService.UserProfileService;
import com.example.mybookshopapp.util.MessageLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/genres")
public class GenreControllerImpl extends ViewControllerImpl {

    private final BookServiceImpl bookService;
    private final GenreServiceImpl genreService;

    @Autowired
    protected GenreControllerImpl(UserProfileService userProfileService, HttpServletRequest request,
                                  BookServiceImpl bookService, GenreServiceImpl genreService,
                                  BookShopService bookShopService, MessageLocale messageLocale) {
        super(userProfileService, request, bookShopService, messageLocale);
        this.bookService = bookService;
        this.genreService = genreService;
    }

    @GetMapping
    public String getPage(Model model) {
        model.addAttribute("genres", genreService.getParentGenreList());
        return "genres/index";
    }

    @GetMapping("/{slug}")
    public String getPage(@PathVariable("slug") String slug, Model model) {
        model.addAttribute("content", genreService.getContent(slug));
        Page<BooksFDto> books = bookService.getPageContents(new BookQuery(0, 20, "genre", slug));
        model.addAttribute("booksGenre", books.getContent());
        model.addAttribute("show", books.getTotalPages() > 1);
        model.addAttribute("totalPages", books.getTotalPages());
        return "genres/slug";
    }
}