package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.author.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}