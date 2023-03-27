package com.example.mybookshopapp.controllers.news;

import com.example.mybookshopapp.data.entity.news.Model;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface ModelController<M extends Model> {

    @GetMapping
    ResponseEntity<Page<M>> getPage(int page, int size, boolean reverse, String sort);
}
