package com.example.mybookshopapp.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto extends Dto {

    private String name;
    private String email;
    private String subject;
    private String text;
    private int userId;

    public MessageDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public MessageDto() {
    }
}
