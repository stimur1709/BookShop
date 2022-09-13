package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.book.links.Book2UserType;
import com.example.mybookshopapp.model.book.links.BookCodeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book2UserTypeRepository extends JpaRepository<Book2UserType, Integer> {

    Book2UserType findByCode(BookCodeType code);

}
