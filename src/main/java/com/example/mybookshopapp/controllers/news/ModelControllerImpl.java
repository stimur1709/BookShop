package com.example.mybookshopapp.controllers.news;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.entity.news.Model;
import com.example.mybookshopapp.service.news.ModelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ModelControllerImpl<M extends Model, D extends Dto, S extends ModelService<M, D>> implements ModelController<M> {

    protected final S service;

    public ModelControllerImpl(S service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Page<M>> getPage(int page, int size, boolean reverse, String sort) {
        PageRequest pageRequest = PageRequest.of(page, size, reverse ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        Page<M> all = service.getPage(pageRequest);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }
}
