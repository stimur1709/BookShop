package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.news.Genre;
import com.example.mybookshopapp.repository.news.ModelRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends ModelRepository<Genre> {

    Genre findGenreEntityBySlug(String slug);

    @Query(value = "select * from genre where parent_id is null", nativeQuery = true)
    List<Genre> getParentGenreList();

}
