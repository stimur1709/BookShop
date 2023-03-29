package com.example.mybookshopapp.controllers.view;

import com.example.mybookshopapp.data.dto.BookQuery;
import com.example.mybookshopapp.data.dto.BooksFDto;
import com.example.mybookshopapp.service.news.AuthorServiceImpl;
import com.example.mybookshopapp.service.news.BookServiceImpl;
import com.example.mybookshopapp.service.userService.UserProfileService;
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

    @Autowired
    protected AuthorControllerImpl(UserProfileService userProfileService, HttpServletRequest request, AuthorServiceImpl authorService, BookServiceImpl bookService) {
        super(userProfileService, request);
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
        Page<BooksFDto> books = bookService.getContents(new BookQuery(0, 5, "author", slug));
        model.addAttribute("authorBooks", books.getContent());
        return "authors/slug";
    }

    @GetMapping("/books/author/{slug}")
    public String authorBooksPage(@PathVariable("slug") String slug, Model model) {
        Page<BooksFDto> books = bookService.getContents(new BookQuery(0, 20, "author", slug));
        model.addAttribute("author", authorService.getContent(slug));
        model.addAttribute("books", books);
        return "books/author";
    }
}