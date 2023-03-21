package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.author.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Page<Author> findDistinctByNameContainsOrBookList_TitleContainsAllIgnoreCase(String name, String title, Pageable pageable);

    Author findAuthorBySlug(String slug);

    Author findAuthorById(Integer id);

}