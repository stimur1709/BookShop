package com.example.mybookshopapp.service.news;

import com.example.mybookshopapp.data.dto.DocumentDto;
import com.example.mybookshopapp.data.entity.other.Document;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.repository.DocumentRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class DocumentService extends ModelServiceImpl<Document, Query, DocumentDto, DocumentRepository> {

    @Autowired
    protected DocumentService(DocumentRepository repository, UserProfileService userProfileService,
                              ModelMapper modelMapper, HttpServletRequest request) {
        super(repository, DocumentDto.class, Document.class, userProfileService, modelMapper, request);
    }

    @Override
    public DocumentDto getContent(String slug) {
        return modelMapper.map(repository.findBySlug(slug), DocumentDto.class);
    }

}
