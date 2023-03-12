package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.BookQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookQueryRepository extends JpaRepository<BookQuery, Integer> {

    @Query(value = "select * from get_books_by_slug(?1, ?2) ", nativeQuery = true)
    BookQuery findBookEntityBySlug(Integer userId, String slug);

}
