package com.example.mybookshopapp.service.news;

import com.example.mybookshopapp.data.dto.GenreDto;
import com.example.mybookshopapp.data.dto.Query;
import com.example.mybookshopapp.data.entity.news.Genre;
import com.example.mybookshopapp.repository.GenreRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl extends ModelServiceImpl<Genre, Query, GenreDto, GenreRepository> {


    @Autowired
    protected GenreServiceImpl(GenreRepository repository, UserProfileService userProfileService, ModelMapper modelMapper) {
        super(repository, GenreDto.class, userProfileService, modelMapper);
    }

    public List<Genre> getParentGenreList() {
        return repository.getParentGenreList();
    }

    @Override
    public GenreDto getContent(String slug) {
        return modelMapper.map(repository.findGenreEntityBySlug(slug), GenreDto.class);
    }

}
