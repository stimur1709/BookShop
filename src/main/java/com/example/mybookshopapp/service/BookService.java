package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.book.file.Book;
import com.example.mybookshopapp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooksData() {
       return bookRepository.findAll();
    }
}
