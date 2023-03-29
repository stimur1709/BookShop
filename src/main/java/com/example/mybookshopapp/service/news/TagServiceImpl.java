package com.example.mybookshopapp.service.news;

import com.example.mybookshopapp.data.dto.Query;
import com.example.mybookshopapp.data.dto.TagBookDto;
import com.example.mybookshopapp.data.entity.news.TagBook;
import com.example.mybookshopapp.repository.news.TagRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl extends ModelServiceImpl<TagBook, Query, TagBookDto, TagRepository> {

    @Autowired
    protected TagServiceImpl(TagRepository repository, UserProfileService userProfileService, ModelMapper modelMapper) {
        super(repository, TagBookDto.class, userProfileService, modelMapper);
    }

    public List<TagBook> getPageOfTagsBooks() {
        return repository.findAll();
    }

    @Override
    public TagBookDto getContent(String slug) {
        return modelMapper.map(repository.findTagEntityBySlug(slug), TagBookDto.class);
    }
}