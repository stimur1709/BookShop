package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.author.AuthorDto;
import com.example.mybookshopapp.data.dto.author.AuthorDtoForAuthor;
import com.example.mybookshopapp.data.entity.Author;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.repository.AuthorRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl extends ModelServiceImpl<Author, Query, AuthorDtoForAuthor, AuthorDtoForAuthor, AuthorRepository> {

    @Autowired
    protected AuthorServiceImpl(AuthorRepository repository, UserProfileService userProfileService,
                                ModelMapper modelMapper, HttpServletRequest request) {
        super(repository, AuthorDtoForAuthor.class, AuthorDtoForAuthor.class, Author.class, userProfileService, modelMapper, request);
    }

    @Override
    public AuthorDtoForAuthor getContent(String slug) {
        return modelMapper.map(repository.findAuthorBySlug(slug), AuthorDtoForAuthor.class);
    }

    @Override
    public Page<AuthorDtoForAuthor> getContents(Query query) {
        if (query.getSearch() != null || !query.getSearch().isBlank()) {
            return repository.getAuthors(query.getSearch(), getPageRequest(query))
                    .map(m -> modelMapper.map(m, AuthorDtoForAuthor.class));
        }
        return super.getContents(query);
    }

    public TreeMap<String, List<AuthorDto>> getAuthorsMap() {
        return new TreeMap<>(repository.findAll().stream().map(m -> modelMapper.map(m, AuthorDto.class))
                .collect(Collectors.groupingBy((AuthorDto a) -> a.getName().substring(0, 1))));
    }

}