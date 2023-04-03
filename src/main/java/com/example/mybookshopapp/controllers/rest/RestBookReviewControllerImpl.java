package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.BookReviewDto;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.service.news.BookReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
public class RestBookReviewControllerImpl extends RestDataControllerImpl<Query, BookReviewDto, BookReviewServiceImpl> {

    @Autowired
    public RestBookReviewControllerImpl(BookReviewServiceImpl service) {
        super(service);
    }

}
