package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.links.Book2User;
import com.example.mybookshopapp.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Book2UserRepository extends JpaRepository<Book2User, Integer> {

    List<Book2User> findBook2UserEntitiesByBookId(Integer book);

    Optional<Book2User> findByUserIdAndBookId(Integer user, Integer book);
}
