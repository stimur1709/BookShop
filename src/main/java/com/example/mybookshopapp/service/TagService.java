package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.tag.Tag;
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

    public List<Tag> getPageOfTagsBooks() {
        addAmount();
        return tagRepository.findAll();
    }

    public Tag getPageBySlug(String slug) {
        return tagRepository.findTagEntityBySlug(slug);
    }

    public void addAmount() {
        List<Tag> bookList = tagRepository.findAll();
        bookList.forEach(tagEntity -> {
            tagEntity.setAmount(tagEntity.getBookList().size());
            tagRepository.save(tagEntity);
        });
    }

    public List<Tag> getTagsByBook(Integer id) {
        return bookRepository.findById(id).map(Book::getTagList).orElse(Collections.emptyList());
    }
}