package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BookRateRequestDto;
import com.example.mybookshopapp.dto.BookReviewRequestDto;
import com.example.mybookshopapp.dto.ResponseResultDto;
import com.example.mybookshopapp.dto.ReviewLikeDto;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.service.*;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
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
import org.springframework.web.servlet.LocaleResolver;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@Controller
public class BookPageController extends ModelAttributeController {

    private final BookService bookService;
    private final ResourceStorage storage;
    private final BooksRatingAndPopularityService ratingBook;
    private final BookReviewService bookReviewService;
    private final BookRateReviewService bookRateReviewService;

    @Autowired
    public BookPageController(BookService bookService, ResourceStorage storage,
                              BooksRatingAndPopularityService ratingBook, BookReviewService bookReviewService,
                              BookRateReviewService bookRateReviewService, UserProfileService userProfileService,
                              BookShopService bookShopService, MessageSource messageSource, LocaleResolver localeResolver) {
        super(userProfileService, bookShopService, messageSource, localeResolver);
        this.bookService = bookService;
        this.storage = storage;
        this.ratingBook = ratingBook;
        this.bookReviewService = bookReviewService;
        this.bookRateReviewService = bookRateReviewService;
    }

    @GetMapping("/books/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model) {
        Book book = bookService.getBookBySlug(slug);
        model.addAttribute("book", book);
        model.addAttribute("userRate", ratingBook.getRateByUserAndBook(book));
        model.addAttribute("sizeofRatingValue", ratingBook.getSizeofRatingValue(book.getId()));
        model.addAttribute("reviews", bookReviewService.getBookReview(book));
        model.addAttribute("rateReview", bookRateReviewService.ratingCalculation(book.getId()));
        model.addAttribute("status", bookShopService.getBookStatus(book));
        return "books/slug";
    }

    @PostMapping("/books/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file, @PathVariable("slug") String slug) throws IOException {
        String savePath = storage.saveNewBookImage(file, slug);
        Book bookToUpdate = bookService.getBookBySlug(slug);
        bookToUpdate.setImage(savePath);
        bookService.save(bookToUpdate);
        return "redirect:/books/" + slug;
    }

    @GetMapping("/books/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash) throws IOException {
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
