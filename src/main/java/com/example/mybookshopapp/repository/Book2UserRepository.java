package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.book.links.Book2User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Book2UserRepository extends JpaRepository<Book2User, Integer> {

    List<Book2User> findBook2UserEntitiesByBookId(Integer book);
}
