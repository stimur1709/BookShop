package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    Page<Book> findBookEntityByTitleContainingAllIgnoreCase(String bookTitle, Pageable nextPage);

    Page<Book> findBookEntityByPubDateBetween(Date from, Date to, Pageable nextPage);

    Page<Book> findByTagList_Slug(String slug, Pageable pageable);

    Page<Book> getByGenreList_Slug(String slug, Pageable pageable);

    Page<Book> getByAuthorList_Id(Integer id, Pageable nextPage);

    Book findBookEntityBySlug(String slug);

    List<Book> findBookEntitiesBySlugIn(Collection<String> slug);

    
}
