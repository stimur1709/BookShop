package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.AuthorDto;
import com.example.mybookshopapp.data.dto.Query;
import com.example.mybookshopapp.service.news.AuthorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authors")
public class RestAuthorControllerImpl extends RestDataControllerImpl<Query, AuthorDto, AuthorServiceImpl> {

    @Autowired
    public RestAuthorControllerImpl(AuthorServiceImpl service) {
        super(service);
    }

}
