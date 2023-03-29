package com.example.mybookshopapp.controllers.view;

import com.example.mybookshopapp.data.dto.*;
import com.example.mybookshopapp.service.*;
import com.example.mybookshopapp.service.news.BookServiceImpl;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@Controller
public class BookControllerImpl extends ViewControllerImpl {

    private final BookServiceImpl bookService;
    private final BooksRatingAndPopularityService ratingBook;
    private final BookReviewService bookReviewService;
    private final BookRateReviewService bookRateReviewService;
    private final DownloadService downloadService;
    private final ResourceStorage storage;

    protected BookControllerImpl(UserProfileService userProfileService, HttpServletRequest request, ResourceStorage storage, BookServiceImpl bookService, BooksRatingAndPopularityService ratingBook, BookReviewService bookReviewService, BookRateReviewService bookRateReviewService, DownloadService downloadService, ResourceStorage storage1) {
        super(userProfileService, request);
        this.bookService = bookService;
        this.ratingBook = ratingBook;
        this.bookReviewService = bookReviewService;
        this.bookRateReviewService = bookRateReviewService;
        this.downloadService = downloadService;
        this.storage = storage1;
    }


    @GetMapping("/books/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model) {
        model.addAttribute("book", bookService.getContent(slug));
        model.addAttribute("reviews", bookReviewService.getBookReview(slug));
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

    @PostMapping(value = "/api/rateBook")
    @ResponseBody
    public ResponseResultDto rateBook(@RequestBody BookRateRequestDto rate) {
        return ratingBook.changeRateBook(rate.getBookId(), rate.getValue());
    }

    @PostMapping("/api/bookReview")
    @ResponseBody
    public ResponseResultDto saveBookReview(@RequestBody @Valid BookReviewRequestDto review, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = null;
            for (FieldError fieldError : bindingResult.getFieldErrors())
                error = fieldError.getDefaultMessage();
            return new ResponseResultDto(false, error);
        }
        return bookReviewService.saveBookReview(review.getBookId(), review.getText());
    }

    @PostMapping("/api/rateBookReview")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> rateBookReview(@RequestBody ReviewLikeDto reviewLikeDto) {
        Map<String, Object> response = bookRateReviewService.changeRateBookReview(reviewLikeDto.getReviewid(), reviewLikeDto.getValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
