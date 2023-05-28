package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.errors.DefaultException;
import com.example.mybookshopapp.service.ModelService;
import com.example.mybookshopapp.util.BindingResultResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

public abstract class RestDataControllerImpl<Q extends Query, D extends Dto, O extends Dto, S extends ModelService<Q, D, O>>
        implements RestDataController<D, O, Q> {

    protected final S service;

    protected RestDataControllerImpl(S service) {
        this.service = service;
    }

    @Override
    public Page<D> getPage(Q q) {
        return service.getPageContents(q);
    }

    @Override
    public ResponseEntity<?> save(O dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(BindingResultResponse.getMessage(bindingResult), HttpStatus.CONFLICT);
        }
        try {
            return new ResponseEntity<>(service.save(dto), HttpStatus.OK);
        } catch (DefaultException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @Override
    public ResponseEntity<D> getContent(String slug) {
        try {
            return new ResponseEntity<>(service.getContent(slug), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<D> getList(Q query) {
        return service.getListContents(query);
    }
}
