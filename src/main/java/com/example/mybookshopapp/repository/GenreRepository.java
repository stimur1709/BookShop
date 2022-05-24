package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.genre.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {

    GenreEntity findGenreEntityBySlug(String slug);

    GenreEntity findGenreEntityById(Integer id);
}
