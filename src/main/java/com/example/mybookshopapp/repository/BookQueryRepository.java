package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.books.BookF;
import org.springframework.data.jpa.repository.Query;

public interface BookQueryRepository extends ModelRepository<BookF> {

    @Query(value = "select * from get_books_by_slug(?1, ?2) ", nativeQuery = true)
    BookF getBook(Integer userId, String slug);

}
