package com.example.mybookshopapp.service.news;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.entity.news.Model;
import com.example.mybookshopapp.repository.news.ModelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public abstract class ModelServiceImpl<M extends Model, D extends Dto, R extends ModelRepository<M>>
        implements ModelService<M, D> {

    protected final R repository;

    public ModelServiceImpl(R repository) {
        this.repository = repository;
    }

    @Override
    public Page<M> getPage(PageRequest pageRequest) {
        return repository.findAll(pageRequest);
    }
}
