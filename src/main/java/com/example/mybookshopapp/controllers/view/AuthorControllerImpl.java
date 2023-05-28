package com.example.mybookshopapp.controllers.view;

import com.example.mybookshopapp.data.dto.book.BooksFDto;
import com.example.mybookshopapp.data.query.BookQuery;
import com.example.mybookshopapp.service.AuthorServiceImpl;
import com.example.mybookshopapp.service.BookServiceImpl;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.user.UserProfileService;
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
@RequestMapping("/authors")
public class AuthorControllerImpl extends ViewControllerImpl {

    private final AuthorServiceImpl authorService;
    private final BookServiceImpl bookService;

    private static final String AUTHOR = "author";

    @Autowired
    protected AuthorControllerImpl(UserProfileService userProfileService, HttpServletRequest request,
                                   AuthorServiceImpl authorService, BookServiceImpl bookService,
                                   BookShopService bookShopService, MessageLocale messageLocale) {
        super(userProfileService, request, bookShopService, messageLocale);
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping
    public String authorsPage(Model model) {
        model.addAttribute("authorsMap", authorService.getAuthorsMap());
        return "authors/index";
    }

    @GetMapping("/{slug}")
    public String authorPage(@PathVariable("slug") String slug, Model model) {
        model.addAttribute("content", authorService.getContent(slug));
        Page<BooksFDto> books = bookService.getPageContents(new BookQuery(0, 5, AUTHOR, slug));
        model.addAttribute("authorBooks", books.getContent());
        return "authors/slug";
    }

    @GetMapping("{slug}/books")
    public String authorBooksPage(@PathVariable("slug") String slug, Model model) {
        Page<BooksFDto> books = bookService.getPageContents(new BookQuery(0, 20, AUTHOR, slug));
        model.addAttribute(AUTHOR, authorService.getContent(slug));
        model.addAttribute("books", books);
        return "books/author";
    }
}