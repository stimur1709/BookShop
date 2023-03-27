package com.example.mybookshopapp.service.news;

import com.example.mybookshopapp.data.dto.Dto;
import com.example.mybookshopapp.data.entity.news.BooksQueryNew;
import com.example.mybookshopapp.repository.news.BooksQueryRepositoryNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class BooksService extends ModelServiceImpl<BooksQueryNew, Dto, BooksQueryRepositoryNew> {


    @Autowired
    public BooksService(BooksQueryRepositoryNew repository) {
        super(repository);
    }

    @Override
    public Page<BooksQueryNew> getPage(PageRequest pageRequest) {
        return repository.getBooks(1, pageRequest);
    }
}
