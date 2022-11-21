package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.BookRating;
import com.example.mybookshopapp.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRatingRepository extends JpaRepository<BookRating, Integer> {

    Optional<BookRating> findByBookAndUser(Book book, User user);

    List<BookRating> findByBook_id(int id);

}
