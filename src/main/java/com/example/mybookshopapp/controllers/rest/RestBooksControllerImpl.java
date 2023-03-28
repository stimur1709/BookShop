package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.BookQuery;
import com.example.mybookshopapp.data.dto.BooksFDto;
import com.example.mybookshopapp.service.news.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class RestBooksControllerImpl extends RestDataControllerImpl<BookQuery, BooksFDto, BookService> {

    @Autowired
    public RestBooksControllerImpl(BookService service) {
        super(service);
    }


}
