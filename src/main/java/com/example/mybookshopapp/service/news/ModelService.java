package com.example.mybookshopapp.service.news;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.entity.news.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ModelService<M extends Model, D extends Dto> {

    Page<M> getPage(PageRequest pageRequest);
}
