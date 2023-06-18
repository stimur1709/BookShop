package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.errors.DefaultException;
import com.example.mybookshopapp.service.ModelService;
import com.example.mybookshopapp.util.BindingResultResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

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
            return status(CONFLICT).body(BindingResultResponse.getMessage(bindingResult));
        }
        try {
            return ok(service.save(dto));
        } catch (DefaultException e) {
            return status(CONFLICT).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<D> getContent(String slug) {
        try {
            return ok(service.getContent(slug));
        } catch (IllegalArgumentException ex) {
            return status(NOT_FOUND).build();
        }
    }

    @Override
    public List<D> getList(Q query) {
        return service.getListContents(query);
    }
}
