package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.service.*;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.entity.book.BookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

@Controller
public class BookPageController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final TagService tagService;
    private final ResourceStorage storage;
    private final BooksRatingAndPopularityService ratingBook;
    private final BookReviewService bookReviewService;

    @Autowired
    public BookPageController(BookService bookService, AuthorService authorService,
                              TagService tagService, ResourceStorage storage, BooksRatingAndPopularityService ratingBook, BookReviewService bookReviewService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.tagService = tagService;
        this.storage = storage;
        this.ratingBook = ratingBook;
        this.bookReviewService = bookReviewService;
    }

    @GetMapping("/books/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model) {
        BookEntity book = bookService.getBookBySlug(slug);
        model.addAttribute("slugBook", book);
        model.addAttribute("authorsBook", authorService.getAuthorsByBook(book.getId()));
        model.addAttribute("tagsBook", tagService.getTagsByBook(book.getId()));
        model.addAttribute("searchWordDto", new SearchWordDto());
        model.addAttribute("searchResult", new ArrayList<>());
        model.addAttribute("sizeOfScore1", ratingBook.getRatingBook(book.getId(), 1));
        model.addAttribute("sizeOfScore2", ratingBook.getRatingBook(book.getId(), 2));
        model.addAttribute("sizeOfScore3", ratingBook.getRatingBook(book.getId(), 3));
        model.addAttribute("sizeOfScore4", ratingBook.getRatingBook(book.getId(), 4));
        model.addAttribute("sizeOfScore5", ratingBook.getRatingBook(book.getId(), 5));
        return "books/slug";
    }

    @PostMapping("/books/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file, @PathVariable("slug") String slug) throws IOException {
        String savePath = storage.saveNewBookImage(file, slug);
        BookEntity bookToUpdate = bookService.getBookBySlug(slug);
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

    @PostMapping("/api/rateBook")
    @ResponseBody
    public Boolean rateBook(@RequestParam("bookId") int bookId, @RequestParam("value") int value) {
        ratingBook.rateBook(bookId, value);
        return true;
    }

    @PostMapping("/api/bookReview")
    @ResponseBody
    public Boolean saveBookReview(@RequestParam("bookId") int bookId, @RequestParam("text") String text) {
        bookReviewService.saveBookReview(bookId, text);
        return true;
    }
}
