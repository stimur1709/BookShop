package com.example.mybookshopapp.service.news;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.entity.Models;
import com.example.mybookshopapp.data.entity.links.BookCodeType;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.repository.news.ModelRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ModelServiceImpl<M extends Models, Q extends Query, D extends Dto, R extends ModelRepository<M>>
        implements ModelService<Q, D> {

    protected final R repository;
    private final Class<D> dto;
    private final Class<M> model;
    protected final UserProfileService userProfileService;
    protected final ModelMapper modelMapper;
    protected final HttpServletRequest request;

    protected ModelServiceImpl(R repository, Class<D> dto, Class<M> model, UserProfileService userProfileService, ModelMapper modelMapper, HttpServletRequest request) {
        this.repository = repository;
        this.dto = dto;
        this.model = model;
        this.userProfileService = userProfileService;
        this.modelMapper = modelMapper;
        this.request = request;
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

    @Override
    public List<D> getAllContents(Sort sort) {
        return repository.findAll(sort).stream().map(m -> modelMapper.map(m, dto)).collect(Collectors.toList());
    }

    @Override
    public D save(D dto) {
        return modelMapper.map(repository.save(modelMapper.map(dto, this.model)), this.dto);
    }

    @Override
    public D getContent(String slug) {
        return modelMapper.map(repository.getById(Integer.parseInt(slug)), this.dto);
    }

    protected BookCodeType getStatusUser() {
        String url = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);
        BookCodeType status = BookCodeType.UNLINK;
        switch (url) {
            case "cart":
                status = BookCodeType.CART;
                break;
            case "my":
                status = BookCodeType.PAID;
                break;
            case "postponed":
                status = BookCodeType.KEPT;
                break;
            case "myarchive":
                status = BookCodeType.ARCHIVED;
                break;
        }
        return status;
    }

}
