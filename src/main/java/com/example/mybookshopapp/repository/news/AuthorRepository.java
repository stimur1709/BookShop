package com.example.mybookshopapp.repository.news;

import com.example.mybookshopapp.data.entity.news.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends ModelRepository<Author> {

    default Page<Author> getAuthors(String title, Pageable pageable) {
        return findDistinctByNameContainsOrBookList_TitleContainsAllIgnoreCase(title, title, pageable);
    }

    Page<Author> findDistinctByNameContainsOrBookList_TitleContainsAllIgnoreCase(String name, String title, Pageable pageable);

    Author findAuthorBySlug(String slug);

}