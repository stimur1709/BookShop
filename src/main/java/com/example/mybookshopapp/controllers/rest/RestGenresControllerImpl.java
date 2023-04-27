package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.GenreDto;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.service.GenreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/genres")
public class RestGenresControllerImpl
        extends RestDataControllerImpl<Query, GenreDto, GenreDto, GenreServiceImpl> {

    @Autowired
    public RestGenresControllerImpl(GenreServiceImpl service) {
        super(service);
    }

}
