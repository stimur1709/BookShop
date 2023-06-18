package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.TagBookDto;
import com.example.mybookshopapp.data.entity.TagBook;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.repository.TagRepository;
import com.example.mybookshopapp.service.user.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public Page<TagBookDto> getPageContents(Query q) {
        if (q.checkQuery()) {
            return repository.findTags(q.getSearch(), q.getIds(), getPageRequest(q))
                    .map(m -> modelMapper.map(m, TagBookDto.class));
        }
        return super.getPageContents(q);
    }

    @Override
    public TagBookDto getContent(String slug) {
        return modelMapper.map(repository.findTagEntityBySlug(slug), TagBookDto.class);
    }

}