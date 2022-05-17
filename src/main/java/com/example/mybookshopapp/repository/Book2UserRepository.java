package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.book.links.Book2UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Book2UserRepository extends JpaRepository<Book2UserEntity, Integer> {

    List<Book2UserEntity> findBook2UserEntitiesByBookId(Integer book);
}
