package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.author.Author;
import com.example.mybookshopapp.data.genre.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {
}
