package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.tag.TagBookDto;
import com.example.mybookshopapp.data.entity.TagBook;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.repository.TagRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class TagServiceImpl extends ModelServiceImpl<TagBook, Query, TagBookDto, TagBookDto, TagRepository> {

    @Autowired
    protected TagServiceImpl(TagRepository repository, UserProfileService userProfileService,
                             ModelMapper modelMapper, HttpServletRequest request) {
        super(repository, TagBookDto.class, TagBookDto.class, TagBook.class, userProfileService, modelMapper, request);
    }

    public List<TagBook> getPageOfTagsBooks() {
        return repository.findAll();
    }

    @Override
    public TagBookDto getContent(String slug) {
        return modelMapper.map(repository.findTagEntityBySlug(slug), TagBookDto.class);
    }

}