package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.errors.DefaultException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ModelService<Q extends Query, D extends Dto, O extends Dto> {

    Page<D> getContents(Q query);

    PageRequest getPageRequest(Q query);

    D getContent(String slug);

    List<D> getAllContents(Q query);

    O save(O dto) throws DefaultException;

    List<D> saveAll(List<D> dtoList);

}


