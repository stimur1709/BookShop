package com.example.mybookshopapp.data.dto;

import com.example.mybookshopapp.data.entity.BooksQuery;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class BooksPageDto {

    private long count;
    private int totalPages;
    private String sort;
    private List<BooksQuery> books;

    public BooksPageDto(Page<BooksQuery> page) {
        this.books = page.getContent();
        this.totalPages = page.getTotalPages();
        this.count = page.getTotalElements();
        this.sort = page.getSort().toString();
    }

}
