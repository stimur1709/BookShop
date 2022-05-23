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
    private List<BookEntity> bookList;

    public BooksTagPageDto(List<BookEntity> bookList) {
        this.bookList = bookList;
        this.count = bookList.size();
    }
}
