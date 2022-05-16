package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.book.BookEntity;
import com.example.mybookshopapp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookEntity> getBooksData() {
        return bookRepository.findAll();
    }

    public Page<BookEntity> getPageOfRecommendBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<BookEntity> getPageOfRecentBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "pubDate"));
        return bookRepository.findAll(nextPage);
    }

//    public Page<BookEntity> getPageOfPopularBooks(Integer offset, Integer limit) {
//        Pageable nextPage = PageRequest.of(offset, limit);
//        return bookRepository.getBestsellers(nextPage);
//    }

    public Page<BookEntity> getPageOfSearchResultBooks(String wordSearch, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookEntityByTitleContaining(wordSearch, nextPage);
    }
}
