package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BookRateRequestDto;
import com.example.mybookshopapp.dto.BookReviewRequestDto;
import com.example.mybookshopapp.dto.ResponseResultDto;
import com.example.mybookshopapp.dto.ReviewLikeDto;
import com.example.mybookshopapp.service.*;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@Controller
public class BookPageController extends ModelAttributeController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final TagService tagService;
    private final ResourceStorage storage;
    private final BooksRatingAndPopularityService ratingBook;
    private final BookReviewService bookReviewService;
    private final BookRateReviewService bookRateReviewService;

    @Autowired
    public BookPageController(BookService bookService, AuthorService authorService,
                              TagService tagService, ResourceStorage storage,
                              BooksRatingAndPopularityService ratingBook, BookReviewService bookReviewService,
                              BookRateReviewService bookRateReviewService, UserProfileService userProfileService,
                              BookShopService bookShopService) {
        super(userProfileService, bookShopService);
        this.bookService = bookService;
        this.authorService = authorService;
        this.tagService = tagService;
        this.storage = storage;
        this.ratingBook = ratingBook;
        this.bookReviewService = bookReviewService;
        this.bookRateReviewService = bookRateReviewService;
    }

    @GetMapping("/books/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model, HttpServletRequest request) {
        Book book = bookService.getBookBySlug(slug);
        model.addAttribute("slugBook", book);
        model.addAttribute("authorsBook", authorService.getAuthorsByBook(book.getId()));
        model.addAttribute("tagsBook", tagService.getTagsByBook(book.getId()));
        model.addAttribute("numberOfScore1", (int) ratingBook.getSizeofRatingValue(book.getId(), 1));
        model.addAttribute("numberOfScore2", (int) ratingBook.getSizeofRatingValue(book.getId(), 2));
        model.addAttribute("numberOfScore3", (int) ratingBook.getSizeofRatingValue(book.getId(), 3));
        model.addAttribute("numberOfScore4", (int) ratingBook.getSizeofRatingValue(book.getId(), 4));
        model.addAttribute("numberOfScore5", (int) ratingBook.getSizeofRatingValue(book.getId(), 5));
        model.addAttribute("numberOfRating", (int) ratingBook.numberOfRating(book.getId()));
        model.addAttribute("rateBook", book.getRate());
        model.addAttribute("reviews", bookReviewService.getBookReview(book));
        model.addAttribute("rateReview", bookRateReviewService.ratingCalculation(book.getId()));
        model.addAttribute("status", getBookShopService().getBookStatus(request, book));
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
        boolean result = ratingBook.changeRateBook(rate.getBookId(), rate.getValue());
        return new ResponseResultDto(result);
    }

    @PostMapping("/api/bookReview")
    @ResponseBody
    public ResponseResultDto saveBookReview(@RequestBody BookReviewRequestDto review) {
        if (bookReviewService.saveBookReview(review.getBookId(), review.getText()))
            return new ResponseResultDto(true);
        else return new ResponseResultDto(false,
                "Отзыв слишком короткий. Напишите, пожалуйста, более развёрнутый отзыв");
    }

    @PostMapping("/api/rateBookReview")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> rateBookReview(@RequestBody ReviewLikeDto reviewLikeDto) {
        return new ResponseEntity<>(Map.of("result", bookRateReviewService.changeRateBookReview(reviewLikeDto.getReviewid(), reviewLikeDto.getValue())), HttpStatus.OK);
    }
}
