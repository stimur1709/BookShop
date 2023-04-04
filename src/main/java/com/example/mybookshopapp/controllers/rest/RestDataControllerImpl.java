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

public abstract class RestDataControllerImpl<Q extends Query, D extends Dto, S extends ModelService<Q, D>>
        implements RestDataController<D, Q> {

    protected final S service;

    public RestDataControllerImpl(S service) {
        this.service = service;
    }

    @Override
    public Page<D> getPage(Q q) {
        return service.getContents(q);
    }

    @Override
    public ResponseEntity<?> save(D dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(BindingResultResponse.getMessage(bindingResult), HttpStatus.CONFLICT);
        }
        try {
            return new ResponseEntity<>(service.save(dto), HttpStatus.OK);
        } catch (DefaultException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
