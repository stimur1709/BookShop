package com.example.mybookshopapp.service.news;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ModelService<Q extends Query, D extends Dto> {

    Page<D> getContents(Q query);

    PageRequest getPageRequest(Q query);

    D getContent(String slug);

    List<D> getAllContents(Sort sort);

    D save(D dto);
}
