package com.example.mybookshopapp.service.news;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.dto.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ModelService<Q extends Query, D extends Dto> {

    Page<D> getContents(Q query);

    PageRequest getPageRequest(Q query);

    D getContent(String slug);
}
