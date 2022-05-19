package com.example.mybookshopapp.dto;

import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.entity.tag.TagEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@Service
public class BooksTagPageDto {

    private Integer count;
    private List<TagEntity> books;

    public BooksTagPageDto(List<TagEntity> books) {
        this.books = books;
        this.count = books.size();
    }
}
