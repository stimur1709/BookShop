package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.book.file.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
