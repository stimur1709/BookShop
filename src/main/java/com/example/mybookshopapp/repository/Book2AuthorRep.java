package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.book.links.Book2Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book2AuthorRep extends JpaRepository<Book2Author, Integer> {

}
