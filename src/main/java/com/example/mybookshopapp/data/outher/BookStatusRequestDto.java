package com.example.mybookshopapp.data.outher;

import com.example.mybookshopapp.data.entity.links.BookCodeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookStatusRequestDto {
    private String booksIds;
    private BookCodeType status;

    public BookStatusRequestDto(String booksIds, BookCodeType status) {
        this.booksIds = booksIds;
        this.status = status;
    }


}
