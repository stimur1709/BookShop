package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.book.Book;
import com.example.mybookshopapp.data.entity.book.BookRating;
import com.example.mybookshopapp.data.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRatingRepository extends JpaRepository<BookRating, Integer> {

    Optional<BookRating> findByBookAndUser(Book book, User user);

    List<BookRating> findByBook_id(int id);

    @Modifying
    @Transactional
    @Query(value = "insert into book_rating(rating, book_id, user_id) " +
            "values (?1, ?2, ?3) on conflict (book_id, user_id) do update set rating = ?1, book_id = ?2, user_id = ?3", nativeQuery = true)
    void updateRating(int rating, int bookId, int userId);

}
