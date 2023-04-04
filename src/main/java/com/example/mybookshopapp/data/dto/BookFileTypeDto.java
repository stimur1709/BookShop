package com.example.mybookshopapp.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookFileTypeDto extends Dto {

    private String name;
    private String description;
    private List<BookFileDto> bookFileList;
}
