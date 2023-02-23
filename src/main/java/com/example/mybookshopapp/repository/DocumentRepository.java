package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.other.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

    Document findBySlug(String slug);
}
