package com.example.mybookshopapp.data.dto.tag;

import com.example.mybookshopapp.data.dto.book.BooksFDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TagBookDtoForTag extends TagBookDto {

    private List<BooksFDto> bookList;

}
