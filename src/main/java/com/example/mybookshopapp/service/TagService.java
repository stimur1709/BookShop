package com.example.mybookshopapp.service;

import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.entity.tag.TagEntity;
import com.example.mybookshopapp.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagEntity> getPageOfTagsBooks() {
        return tagRepository.findAll();
    }

    public Page<TagEntity> getPageBySlug(String slug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return tagRepository.findTagEntityBySlug(slug, nextPage);
    }
}