package com.example.mybookshopapp.repository.news;

import com.example.mybookshopapp.data.entity.TagBook;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends ModelRepository<TagBook> {

    TagBook findTagEntityBySlug(String slug);
}
