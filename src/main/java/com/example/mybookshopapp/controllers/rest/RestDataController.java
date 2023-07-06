package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.errors.DefaultException;
import com.example.mybookshopapp.errors.RestDefaultException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface RestDataController<D extends Dto, O extends Dto, Q extends Query> {

    @GetMapping
    Page<D> getPage(Q query);

    @GetMapping("/list")
    List<D> getList(Q query);

    @PostMapping("/save")
    ResponseEntity<?> save(@RequestBody @Valid O dto, BindingResult bindingResult);

    @GetMapping("{slug}")
    ResponseEntity<D> getContent(@PathVariable("slug") String slug);

    @DeleteMapping("/delete/{id}")
    ResponseEntity<Object> delete(@PathVariable int id) throws DefaultException, RestDefaultException;

}
