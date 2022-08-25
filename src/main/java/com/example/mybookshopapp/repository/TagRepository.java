package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.tag.TagBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagBook, Integer> {

    TagBook findTagEntityBySlug(String slug);
}
