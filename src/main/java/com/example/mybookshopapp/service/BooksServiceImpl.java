package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.entity.BooksQuery;
import com.example.mybookshopapp.data.entity.book.Book;
import com.example.mybookshopapp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BooksServiceImpl {

    private final BookRepository bookRepository;

    @Autowired
    public BooksServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public Page<BooksQuery> getPageOfSearchResultBooks(String wordSearch, Pageable page) {
        return null;
    }

    public void save(Book bookToUpdate) {
        bookRepository.save(bookToUpdate);
    }


}