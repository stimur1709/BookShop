package com.example.mybookshopapp.repository.news;

import com.example.mybookshopapp.data.entity.news.BooksQueryNew;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface BooksQueryRepositoryNew extends ModelRepository<BooksQueryNew> {

    @Query(value = "select * from get_books(?1)", nativeQuery = true)
    Page<BooksQueryNew> getBooks(Integer userId, Pageable pageable);

}
