package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.books.BooksViewed;
import com.example.mybookshopapp.data.entity.links.key.KeyBook2User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface BooksViewedRepository extends JpaRepository<BooksViewed, KeyBook2User> {

    @Modifying
    @Transactional
    @Async
    @Query(value = "INSERT INTO books_viewed(book_id, user_id) " +
            "VALUES (?1, ?2) " +
            "ON CONFLICT(book_id, user_id) " +
            "DO UPDATE SET book_id = ?1, user_id = ?2, time = now()", nativeQuery = true)
    void insertOrUpdate(int bookId, int userId);

}
