package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.entity.Models;
import com.example.mybookshopapp.data.entity.links.BookCodeType;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.errors.DefaultException;
import com.example.mybookshopapp.repository.ModelRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ModelServiceImpl<M extends Models, Q extends Query, D extends Dto, O extends Dto, R extends ModelRepository<M>>
        implements ModelService<Q, D, O> {

    protected final R repository;
    private final Class<D> dtoS;
    private final Class<O> dto;
    private final Class<M> model;
    protected final UserProfileService userProfileService;
    protected final ModelMapper modelMapper;
    protected final HttpServletRequest request;

    protected ModelServiceImpl(R repository, Class<D> dtoS, Class<O> dto, Class<M> model, UserProfileService userProfileService, ModelMapper modelMapper, HttpServletRequest request) {
        this.repository = repository;
        this.dtoS = dtoS;
        this.dto = dto;
        this.model = model;
        this.userProfileService = userProfileService;
        this.modelMapper = modelMapper;
        this.request = request;
    }

    @Override
    public Page<D> getPageContents(Q q) {
        return repository.findAll(getPageRequest(q)).map(m -> modelMapper.map(m, dtoS));
    }

    @Override
    public PageRequest getPageRequest(Q q) {
        return q.getProperty() == null
                ? PageRequest.of(q.getOffset(), q.getLimit())
                : PageRequest.of(q.getOffset(), q.getLimit(),
                Sort.by(q.isReverse() ? Sort.Direction.ASC : Sort.Direction.DESC, q.getProperty()));
    }

    @Override
    public List<D> getListContents(Q q) {
        Sort sort = Sort.by(q.isReverse() ? Sort.Direction.ASC : Sort.Direction.DESC, q.getProperty());
        return repository.findAll(sort)
                .stream()
                .map(m -> modelMapper.map(m, dtoS))
                .collect(Collectors.toList());
    }

    @Override
    public O save(O dto) throws DefaultException {
        return modelMapper.map(repository.save(modelMapper.map(dto, this.model)), this.dto);
    }

    @Override
    public D getContent(String slug) {
        return modelMapper.map(repository.getById(Integer.parseInt(slug)), this.dtoS);
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

    @Override
    public List<D> saveAll(List<D> dtoList) {
        List<M> mList = dtoList
                .stream()
                .map(m -> modelMapper.map(m, model))
                .collect(Collectors.toList());
        return repository.saveAll(mList)
                .stream()
                .map(m -> modelMapper.map(m, this.dtoS))
                .collect(Collectors.toList());
    }
}
