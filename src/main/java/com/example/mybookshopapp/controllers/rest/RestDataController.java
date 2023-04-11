package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface RestDataController<D extends Dto, O extends Dto, Q extends Query> {

    @GetMapping
    Page<D> getPage(Q query);

    @PostMapping("/save")
    ResponseEntity<?> save(@RequestBody @Valid O dto, BindingResult bindingResult);

    @GetMapping("{slug}")
    ResponseEntity<D> getContent(@PathVariable("slug") String slug);

}
