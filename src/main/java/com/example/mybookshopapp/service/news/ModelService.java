package com.example.mybookshopapp.service.news;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.errors.DefaultException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ModelService<Q extends Query, D extends Dto> {

    Page<D> getContents(Q query);

    PageRequest getPageRequest(Q query);

    D getContent(String slug);

    List<D> getAllContents(Q query);

    D save(D dto) throws DefaultException;
}