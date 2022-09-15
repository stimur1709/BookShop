package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.links.Book2User;
import com.example.mybookshopapp.model.book.links.BookCodeType;
import com.example.mybookshopapp.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Book2UserRepository extends JpaRepository<Book2User, Integer> {

    Optional<Book2User> findByUserAndBook(User user, Book book);

    List<Book2User> findByType_CodeAndUser_Id(BookCodeType code, int id);

}
