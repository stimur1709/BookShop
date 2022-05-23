package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.tag.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {

    TagEntity findTagEntityBySlug(String slug);
}
