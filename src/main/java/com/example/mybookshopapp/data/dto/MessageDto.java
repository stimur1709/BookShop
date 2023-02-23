package com.example.mybookshopapp.data.dto;

import lombok.Data;

@Data
public class MessageDto {

    private String name;
    private String mail;
    private String title;
    private String text;

    public MessageDto(String name, String mail) {
        this.name = name;
        this.mail = mail;
    }

    public MessageDto() {
    }
}
