package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.book.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    Page<BookEntity> findBookEntityByTitleContaining(String bookTitle, Pageable nextPage);

    Page<BookEntity> findBookEntityByPubDateBetween(Date from, Date to, Pageable nextPage);

    @Query(value = "SELECT * FROM books WHERE is_bestseller=1", nativeQuery = true)
    Page<BookEntity> getBestsellers(Pageable nextPage);
}
