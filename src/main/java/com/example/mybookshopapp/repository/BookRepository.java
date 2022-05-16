package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.book.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    Page<BookEntity> findBookEntityByTitleContaining(String bookTitle, Pageable nextPage);

//    @Query("from BookEntity where isBestseller=1")
//    Page<BookEntity> getBestsellers(Pageable nextPage);
}
