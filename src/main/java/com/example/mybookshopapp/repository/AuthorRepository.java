package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.author.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Author findAuthorBySlug(String slug);

    @Query(value = "SELECT * FROM authors " +
            "inner join book2author on authors.id = book2author.author_id " +
            "inner join books on books.id = book2author.book_id " +
            "where books.id = ?1", nativeQuery = true)
    List<Author> getAuthorByBook(Integer id);

    Author findAuthorById(Integer id);
}