package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.tag.TagBook;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final BookRepository bookRepository;

    @Autowired
    public TagService(TagRepository tagRepository, BookRepository bookRepository) {
        this.tagRepository = tagRepository;
        this.bookRepository = bookRepository;
    }

    public List<TagBook> getPageOfTagsBooks() {
        return tagRepository.findAll();
    }

    public TagBook getPageBySlug(String slug) {
        return tagRepository.findTagEntityBySlug(slug);
    }

    public List<TagBook> getTagsByBook(Integer id) {
        return bookRepository.findById(id).map(Book::getTagList).orElse(Collections.emptyList());
    }
}