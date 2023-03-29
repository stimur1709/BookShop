package com.example.mybookshopapp.data.dto;

import com.example.mybookshopapp.data.entity.news.Author;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class AuthorsPageDto {

    private long count;
    private int totalPages;
    private List<Author> content;

    public AuthorsPageDto(Page<Author> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.count = page.getTotalElements();
    }

}
