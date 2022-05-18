package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.tag.TagEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {

    Page<TagEntity> findTagEntityBySlug(String slug, Pageable nextPage);
}
