package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.other.Document;
import com.example.mybookshopapp.repository.news.ModelRepository;

public interface DocumentRepository extends ModelRepository<Document> {

    Document findBySlug(String slug);
}
