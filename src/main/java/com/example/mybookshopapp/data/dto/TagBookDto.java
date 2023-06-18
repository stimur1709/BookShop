package com.example.mybookshopapp.data.dto;

import com.example.mybookshopapp.data.dto.book.BooksFDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TagBookDto extends Dto {

    private String name;
    private String slug;
    private List<BooksFDto> bookList;

}
