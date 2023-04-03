package com.example.mybookshopapp.controllers.view;

import com.example.mybookshopapp.data.dto.BooksFDto;
import com.example.mybookshopapp.data.outher.ReviewLikeDto;
import com.example.mybookshopapp.data.query.BookQuery;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.service.BookRateReviewService;
import com.example.mybookshopapp.service.DownloadService;
import com.example.mybookshopapp.service.ResourceStorage;
import com.example.mybookshopapp.service.news.BookReviewServiceImpl;
import com.example.mybookshopapp.service.news.BookServiceImpl;
import com.example.mybookshopapp.service.news.BookShopService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import com.example.mybookshopapp.util.MessageLocale;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@Controller
public class BookControllerImpl extends ViewControllerImpl {

    private final BookServiceImpl bookService;
    private final BookReviewServiceImpl bookReviewServiceImpl;
    private final BookRateReviewService bookRateReviewService;
    private final DownloadService downloadService;
    private final ResourceStorage storage;

    protected BookControllerImpl(UserProfileService userProfileService, HttpServletRequest request,
                                 ResourceStorage storage, BookServiceImpl bookService,
                                 BookReviewServiceImpl bookReviewServiceImpl, BookRateReviewService bookRateReviewService,
                                 DownloadService downloadService, BookShopService bookShopService,
                                 MessageLocale messageLocale) {
        super(userProfileService, request, bookShopService, messageLocale);
        this.bookService = bookService;
        this.bookReviewServiceImpl = bookReviewServiceImpl;
        this.bookRateReviewService = bookRateReviewService;
        this.downloadService = downloadService;
        this.storage = storage;
    }

    @GetMapping("/books/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model) {
        model.addAttribute("book", bookService.getContent(slug));
        model.addAttribute("reviews", bookReviewServiceImpl.getAllContents(new Query(slug)));
        return "books/slug";
    }

    @GetMapping({"/books/recent", "/books/popular", "/books/viewed"})
    public String recentPage(Model model) {
        String url = getUrl();
        Page<BooksFDto> books = bookService.getContents(new BookQuery(0, 20, getProperty(url), false));
        model.addAttribute("books", books.getContent());
        model.addAttribute("show", books.getTotalPages() > 1);
        model.addAttribute("totalPages", books.getTotalPages());
        return "books/" + url;
    }

    private String getProperty(String url) {
        String property = null;
        switch (url) {
            case "viewed":
                property = "viewed";
                break;
            case "popular":
                property = "popularity";
                break;
            case "recent":
                property = "pub_date";
                break;
        }
        return property;
    }

//    @PostMapping("/books/{slug}/img/save")
//    public String saveNewBookImage(@RequestParam("file") MultipartFile file, @PathVariable("slug") String slug) throws IOException {
//        String savePath = storage.saveNewBookImage(file, slug);
//        Book bookToUpdate = bookService.getBookBySlug(slug);
//        bookToUpdate.setImage(savePath);
//        bookService.save(bookToUpdate);
//        return "redirect:/books/" + slug;
//    }

    @GetMapping("/books/download/{hash}")
    public ResponseEntity<?> bookFile(@PathVariable("hash") String hash) throws IOException {
        downloadService.fileDownload(hash);
        Path path = storage.getBookFilePath(hash);
        MediaType mediaType = storage.getBookFileName(hash);
        byte[] data = storage.getBookFileByteArray(hash);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @PostMapping("/api/rateBookReview")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> rateBookReview(@RequestBody ReviewLikeDto reviewLikeDto) {
        Map<String, Object> response = bookRateReviewService.changeRateBookReview(reviewLikeDto.getReviewid(), reviewLikeDto.getValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
