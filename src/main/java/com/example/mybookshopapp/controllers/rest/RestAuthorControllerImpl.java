package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.author.AuthorDtoForAuthor;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.service.AuthorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authors")
public class RestAuthorControllerImpl
        extends RestDataControllerImpl<Query, AuthorDtoForAuthor, AuthorDtoForAuthor, AuthorServiceImpl> {

    @Autowired
    public RestAuthorControllerImpl(AuthorServiceImpl service) {
        super(service);
    }

}
