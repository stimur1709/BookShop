package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.BooksFDto;
import com.example.mybookshopapp.data.outher.BookRateRequestDto;
import com.example.mybookshopapp.data.outher.BookStatusRequestDto;
import com.example.mybookshopapp.data.outher.ResponseResultDto;
import com.example.mybookshopapp.data.query.BookQuery;
import com.example.mybookshopapp.service.BookServiceImpl;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.BooksRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class RestBooksControllerImpl
        extends RestDataControllerImpl<BookQuery, BooksFDto, BookServiceImpl> {

    private final BookShopService bookShopService;
    private final BooksRatingService booksRatingService;

    @Autowired
    public RestBooksControllerImpl(BookServiceImpl service, BookShopService bookShopService,
                                   BooksRatingService booksRatingService) {
        super(service);
        this.bookShopService = bookShopService;
        this.booksRatingService = booksRatingService;
    }

    @GetMapping(value = {"/size/cart", "/size/postponed"})
    public int getSize() {
        return service.getBookUser().size();
    }


    @PostMapping("/changeBookStatus")
    public ResponseResultDto handlerChangeBookStatus(@RequestBody BookStatusRequestDto dto) {
        return bookShopService.changeBookStatus(dto);
    }

    @PostMapping(value = "/rateBook")
    public ResponseResultDto rateBook(@RequestBody BookRateRequestDto rate) {
        return booksRatingService.changeRateBook(rate.getBookId(), rate.getValue());
    }

    @Override
    public ResponseEntity<?> save(BooksFDto dto, BindingResult bindingResult) {
        return super.save(dto, bindingResult);
    }
}
