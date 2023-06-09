package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.AuthorDto;
import com.example.mybookshopapp.data.entity.Author;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.repository.AuthorRepository;
import com.example.mybookshopapp.service.user.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl extends ModelServiceImpl<Author, Query, AuthorDto, AuthorDto, AuthorRepository> {

    @Autowired
    protected AuthorServiceImpl(AuthorRepository repository, UserProfileService userProfileService,
                                ModelMapper modelMapper, HttpServletRequest request) {
        super(repository, AuthorDto.class, AuthorDto.class, Author.class, userProfileService, modelMapper, request);
    }

    @Override
    public AuthorDto getContent(String slug) {
        return modelMapper.map(repository.findAuthorBySlug(slug), AuthorDto.class);
    }

    @Override
    public Page<AuthorDto> getPageContents(Query q) {
        if (q.checkQuery()) {
            return repository.findAuthors(q.getSearch(), q.getIds(), getPageRequest(q))
                    .map(m -> modelMapper.map(m, AuthorDto.class));
        }
        return super.getPageContents(q);
    }

    public SortedMap<String, List<AuthorDto>> getAuthorsMap() {
        return new TreeMap<>(repository.findAll().stream().map(m -> modelMapper.map(m, AuthorDto.class))
                .collect(Collectors.groupingBy((AuthorDto a) -> a.getName().substring(0, 1))));
    }

}