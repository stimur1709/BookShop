package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.GenreDto;
import com.example.mybookshopapp.data.entity.Genre;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.repository.GenreRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class GenreServiceImpl extends ModelServiceImpl<Genre, Query, GenreDto, GenreRepository> {


    @Autowired
    protected GenreServiceImpl(GenreRepository repository, UserProfileService userProfileService,
                               ModelMapper modelMapper, HttpServletRequest request) {
        super(repository, GenreDto.class, Genre.class, userProfileService, modelMapper, request);
    }

    public List<Genre> getParentGenreList() {
        return repository.getParentGenreList();
    }

    @Override
    public GenreDto getContent(String slug) {
        return modelMapper.map(repository.findGenreEntityBySlug(slug), GenreDto.class);
    }

}
