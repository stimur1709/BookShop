package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.entity.other.Document;
import com.example.mybookshopapp.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document getDocument(String slug) {
        return documentRepository.findBySlug(slug);
    }
}
