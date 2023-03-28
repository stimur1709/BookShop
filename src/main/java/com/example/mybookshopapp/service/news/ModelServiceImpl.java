package com.example.mybookshopapp.service.news;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.dto.Query;
import com.example.mybookshopapp.data.entity.news.Models;
import com.example.mybookshopapp.repository.news.ModelRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public abstract class ModelServiceImpl<M extends Models, Q extends Query, D extends Dto, R extends ModelRepository<M>>
        implements ModelService<Q, D> {

    protected final R repository;
    private final Class<D> dto;
    protected final UserProfileService userProfileService;
    protected final ModelMapper modelMapper;

    protected ModelServiceImpl(R repository, Class<D> dto, UserProfileService userProfileService, ModelMapper modelMapper) {
        this.repository = repository;
        this.dto = dto;
        this.userProfileService = userProfileService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<D> getContents(Q q) {
        return repository.findAll(PageRequest.of(q.getOffset(), q.getLimit())).map(m -> modelMapper.map(m, dto));
    }

    @Override
    public PageRequest getPageRequest(Q q) {
        return q.getProperty() == null
                ? PageRequest.of(q.getOffset(), q.getLimit())
                : PageRequest.of(q.getOffset(), q.getLimit(), Sort.by(q.isReverse() ? Sort.Direction.ASC : Sort.Direction.DESC, q.getProperty()));
    }

}
