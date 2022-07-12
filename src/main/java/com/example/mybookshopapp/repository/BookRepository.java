package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.book.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    Page<BookEntity> findBookEntityByTitleContaining(String bookTitle, Pageable nextPage);

    Page<BookEntity> findBookEntityByPubDateBetween(Date from, Date to, Pageable nextPage);

    @Query(value = "SELECT * FROM books " +
            "inner join book2tag on books.id = book2tag.book_id " +
            "inner join tags on tags.id = book2tag.tag_id " +
            "where tags.slug = ?1", nativeQuery = true)
    Page<BookEntity> getBookByTag(String slug, Pageable pageable);

    @Query(value = "SELECT * FROM books " +
            "inner join book2genre on books.id = book2genre.book_id " +
            "inner join genre on genre.id = book2genre.genre_id " +
            "where genre.slug = ?1", nativeQuery = true)
    Page<BookEntity> getBookByGenre(String slug, Pageable nextPage);

    @Query(value = "SELECT * FROM books " +
            "inner join book2author on books.id = book2author.book_id " +
            "inner join authors on authors.id = book2author.author_id " +
            "where authors.id = ?1 order by books.pub_date desc", nativeQuery = true)
    Page<BookEntity> getBookByAuthor(Integer id, Pageable nextPage);

    BookEntity findBookEntityBySlug(String slug);

    @Query(value = "SELECT count(id) from books", nativeQuery = true)
    Integer getNumbersOffAllBooks();

    List<BookEntity> findBookEntitiesBySlugIn(String[] slugs);
}
