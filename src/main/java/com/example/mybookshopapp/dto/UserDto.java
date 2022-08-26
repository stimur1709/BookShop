package com.example.mybookshopapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String name;
    private String mail;
    private String phone;

    public UserDto(String name, String mail, String phone) {
        this.name = name;
        this.mail = mail;
        this.phone = phone;
    }
}
