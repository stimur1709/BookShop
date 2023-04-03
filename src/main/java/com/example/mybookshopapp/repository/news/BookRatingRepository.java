package com.example.mybookshopapp.repository.news;

import com.example.mybookshopapp.data.entity.books.BookRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BookRatingRepository extends JpaRepository<BookRating, Integer> {

    List<BookRating> findByBookId(int id);

    @Transactional
    @Query(value = "insert into book_rating(rating, book_id, user_id) " +
            "values (?1, ?2, ?3) on conflict (book_id, user_id) do update set rating = ?1, book_id = ?2, user_id = ?3 " +
            "returning (select count(*) from book_rating where book_id = ?2) + 1", nativeQuery = true)
    int updateRating(int rating, int bookId, int userId);

}
