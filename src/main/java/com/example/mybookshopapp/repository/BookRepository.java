package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.books.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("select b from Book b where upper(b.title) like upper(concat('%', ?1, '%'))")
    Page<Book> findBookEntityByTitleContainingAllIgnoreCase(String bookTitle, Pageable nextPage);

    Book findBookEntityBySlug(String slug);

    List<Book> findBookEntitiesBySlugIn(Collection<String> slug);

}
