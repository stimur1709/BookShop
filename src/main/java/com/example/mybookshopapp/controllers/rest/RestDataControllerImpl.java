package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.dto.Query;
import com.example.mybookshopapp.service.news.ModelService;
import org.springframework.data.domain.Page;

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
}
