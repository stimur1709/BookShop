package com.example.mybookshopapp.service.news;

import com.example.mybookshopapp.data.dto.AuthorDto;
import com.example.mybookshopapp.data.dto.Query;
import com.example.mybookshopapp.data.entity.news.Author;
import com.example.mybookshopapp.repository.news.AuthorRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl extends ModelServiceImpl<Author, Query, AuthorDto, AuthorRepository> {

    @Autowired
    protected AuthorServiceImpl(AuthorRepository repository, UserProfileService userProfileService, ModelMapper modelMapper) {
        super(repository, AuthorDto.class, userProfileService, modelMapper);
    }

    @Override
    public AuthorDto getContent(String slug) {
        return modelMapper.map(repository.findAuthorBySlug(slug), AuthorDto.class);
    }

    @Override
    public Page<AuthorDto> getContents(Query query) {
        if (query.getSearch() != null || !query.getSearch().isBlank()) {
            return repository.getAuthors(query.getSearch(), getPageRequest(query))
                    .map(m -> modelMapper.map(m, AuthorDto.class));
        }
        return super.getContents(query);
    }

    public TreeMap<String, List<AuthorDto>> getAuthorsMap() {
        return new TreeMap<>(repository.findAll().stream().map(m -> modelMapper.map(m, AuthorDto.class))
                .collect(Collectors.groupingBy((AuthorDto a) -> a.getName().substring(0, 1))));
    }

}