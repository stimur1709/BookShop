package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.book.links.Book2User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Book2UserRepository extends JpaRepository<Book2User, Integer> {

    Optional<Book2User> findByUserIdAndBookId(Integer user, Integer book);

}
