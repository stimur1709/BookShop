package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.TagBook;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends ModelRepository<TagBook> {

    TagBook findTagEntityBySlug(String slug);
}
