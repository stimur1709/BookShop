package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.BooksFDto;
import com.example.mybookshopapp.data.outher.BookStatusRequestDto;
import com.example.mybookshopapp.data.outher.ResponseResultDto;
import com.example.mybookshopapp.data.query.BookQuery;
import com.example.mybookshopapp.service.news.BookServiceImpl;
import com.example.mybookshopapp.service.news.BookShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class RestBooksControllerImpl extends RestDataControllerImpl<BookQuery, BooksFDto, BookServiceImpl> {

    private final BookShopService bookShopService;

    @Autowired
    public RestBooksControllerImpl(BookServiceImpl service, BookShopService bookShopService) {
        super(service);
        this.bookShopService = bookShopService;
    }

    @GetMapping(value = {"/size/cart", "/size/postponed"})
    @ResponseBody
    public int getSize() {
        return service.getBookUser().size();
    }


    @PostMapping("/changeBookStatus")
    public ResponseResultDto handlerChangeBookStatus(@RequestBody BookStatusRequestDto dto) {
        return bookShopService.changeBookStatus(dto);
    }

}
