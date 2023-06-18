package com.example.mybookshopapp.controllers.view;

import com.example.mybookshopapp.data.dto.book.BooksFDto;
import com.example.mybookshopapp.data.query.BookQuery;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.service.BookReviewServiceImpl;
import com.example.mybookshopapp.service.BookServiceImpl;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.DownloadService;
import com.example.mybookshopapp.service.user.UserProfileService;
import com.example.mybookshopapp.util.MessageLocale;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class BookControllerImpl extends ViewControllerImpl {

    private final BookServiceImpl bookService;
    private final BookReviewServiceImpl bookReviewService;
    private final DownloadService downloadService;

    protected BookControllerImpl(UserProfileService userProfileService, HttpServletRequest request, BookServiceImpl bookService,
                                 BookReviewServiceImpl bookReviewService,
                                 DownloadService downloadService, BookShopService bookShopService,
                                 MessageLocale messageLocale) {
        super(userProfileService, request, bookShopService, messageLocale);
        this.bookService = bookService;
        this.bookReviewService = bookReviewService;
        this.downloadService = downloadService;
    }

    @GetMapping("/books/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model) {
        model.addAttribute("book", bookService.getContent(slug));
        model.addAttribute("reviews", bookReviewService.getListContents(new Query(slug)));
        return "books/slug";
    }

    @GetMapping({"/books/recent", "/books/popular", "/books/viewed"})
    public String recentPage(Model model) {
        String url = getUrl();
        Page<BooksFDto> books = bookService.getPageContents(new BookQuery(0, 20, getProperty(url), false));
        model.addAttribute("books", books.getContent());
        model.addAttribute("show", books.getTotalPages() > 1);
        model.addAttribute("totalPages", books.getTotalPages());
        return "books/" + url;
    }

    private String getProperty(String url) {
        String property;
        switch (url) {
            case "viewed":
                property = "viewed";
                break;
            case "popular":
                property = "popularity";
                break;
            default:
                property = "pub_date";
                break;
        }
        return property;
    }

    @GetMapping("/books/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash) {
        try {
            return downloadService.fileDownload(hash);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
