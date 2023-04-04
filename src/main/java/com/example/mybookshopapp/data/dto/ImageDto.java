package com.example.mybookshopapp.data.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageDto extends Dto {

    private String name;
    private long size;

    public ImageDto(String name, long size) {
        this.name = name;
        this.size = size;
    }
}
