package com.example.mybookshopapp.controllers.news;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.entity.news.BooksQueryNew;
import com.example.mybookshopapp.service.news.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class BooksController extends ModelControllerImpl<BooksQueryNew, Dto, BooksService> {

    @Autowired
    public BooksController(BooksService service) {
        super(service);
    }


}
