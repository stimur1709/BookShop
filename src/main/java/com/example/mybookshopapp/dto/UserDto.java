package com.example.mybookshopapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String name;
    private String mail;
    private String phone;
    private int balance;

    public UserDto(String name, String mail, String phone, int balance) {
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.balance = balance;
    }

    public UserDto(String name) {
        this.name = name;
    }
}
