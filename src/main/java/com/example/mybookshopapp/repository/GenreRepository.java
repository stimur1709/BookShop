package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.genre.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {

    Genre findGenreEntityBySlug(String slug);

    Genre findGenreEntityById(Integer id);
}
