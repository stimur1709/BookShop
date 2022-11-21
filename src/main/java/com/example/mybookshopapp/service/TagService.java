package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.tag.TagBook;
import com.example.mybookshopapp.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagBook> getPageOfTagsBooks() {
        return tagRepository.findAll();
    }

    public TagBook getPageBySlug(String slug) {
        return tagRepository.findTagEntityBySlug(slug);
    }

}