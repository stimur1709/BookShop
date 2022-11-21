package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.model.author.Author;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.service.AuthorService;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

@Controller
@Tag(name = "Страница авторов", description = "На странице размещается список ссылок на всех авторов, " +
        "отсортированный по алфавиту и сгруппированы по буквам алфавита. " +
        "Каждая ссылка ведет на страницу соответствующего автора.")
public class AuthorsPageController extends ModelAttributeController {

    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public AuthorsPageController(AuthorService authorService, BookService bookService,
                                 UserProfileService userProfileService, BookShopService bookShopService,
                                 MessageSource messageSource, LocaleResolver localeResolver) {
        super(userProfileService, bookShopService, messageSource, localeResolver);
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping("/authors")
    public String authorsPage(Model model) {
        model.addAttribute("authorsMap", authorService.getAuthorsMap());
        return "authors/index";
    }

    @GetMapping("/authors/{slug}")
    public String authorPage(@PathVariable("slug") String slug, Model model) {
        Author author = authorService.getAuthorsBySlug(slug);
        model.addAttribute("author", author);
        model.addAttribute("authorBooks", bookService.getBooksForPageAuthor(author, 0, 5).getContent());
        return "authors/slug";
    }

    @GetMapping("/books/author/{slug}")
    public String authorBooksPage(@PathVariable("slug") String slug, Model model) {
        Author author = authorService.getAuthorsBySlug(slug);
        Page<Book> books = bookService.getBooksForPageAuthor(author, 0, 20);
        model.addAttribute("author", author);
        model.addAttribute("books", books);
        return "books/author";
    }

    @GetMapping("/api/books/author/{id}")
    @ResponseBody
    @Operation(summary = "Постраничный вывод книг",
            description = "Выводит на странице книги, привязанных к данному автору")
    public BooksPageDto authorBooksPage(@PathVariable("id") Integer id, @RequestParam("offset") Integer offset,
                                        @RequestParam("limit") Integer limit) {
        Author author = authorService.getAuthorsById(id);
        return new BooksPageDto(bookService.getBooksForPageAuthor(author, offset, limit).getContent());
    }
}