package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    Tag findTagEntityBySlug(String slug);
}
