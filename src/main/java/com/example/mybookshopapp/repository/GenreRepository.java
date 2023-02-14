package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.genre.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {

    Genre findGenreEntityBySlug(String slug);

    @Query(value = "select * from genre where parent_id is null", nativeQuery = true)
    List<Genre> getParentGenreList();

}
