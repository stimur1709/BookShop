package com.example.mybookshopapp.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookFileDto extends Dto {

    private String hash;
    private BookFileTypeDto bookFileType;
    private String path;
    private Integer bookId;
}
