package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.book.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {
}
