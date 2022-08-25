package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.author.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Author findAuthorBySlug(String slug);

    Author findAuthorById(Integer id);
}