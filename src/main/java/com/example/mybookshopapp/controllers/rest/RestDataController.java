package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.dto.Query;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;

public interface RestDataController<D extends Dto, Q extends Query> {

    @GetMapping
    Page<D> getPage(Q query);
}
