package com.example.mybookshopapp.service;

import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.entity.tag.TagEntity;
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

    public List<TagEntity> getPageOfTagsBooks() {
        addAmount();
        return tagRepository.findAll();
    }

    public TagEntity getPageBySlug(String slug) {
        return tagRepository.findTagEntityBySlug(slug);
    }

    public void addAmount() {
        List<TagEntity> bookList = tagRepository.findAll();
        bookList.forEach(tagEntity -> {
            tagEntity.setAmount(tagEntity.getBookList().size());
            tagRepository.save(tagEntity);
        });
    }

    public List<TagEntity> getTagsByBook(Integer id) {
        return tagRepository.getBookByTag(id);
    }
}