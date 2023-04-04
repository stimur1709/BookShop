package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.other.Document;

public interface DocumentRepository extends ModelRepository<Document> {

    Document findBySlug(String slug);
}
