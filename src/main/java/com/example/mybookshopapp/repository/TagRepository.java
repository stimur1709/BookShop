package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.tag.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {

    TagEntity findTagEntityBySlug(String slug);

    @Query(value = "SELECT * FROM tags " +
            "inner join book2tag on tags.id = book2tag.tag_id " +
            "inner join books on books.id = book2tag.book_id " +
            "where books.id = ?1", nativeQuery = true)
    List<TagEntity> getBookByTag(Integer id);
}
