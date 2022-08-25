package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.book.links.Book2User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Book2UserRepository extends JpaRepository<Book2User, Integer> {

    List<Book2User> findBook2UserEntitiesByBookId(Integer book);
}
