package com.example.mybookshopapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private int id;
    private String firstname;
    private String lastname;
    private String mail;
    private String phone;
    private int balance;

    public UserDto(int id, String firstname,String lastname, String mail, String phone, int balance) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.mail = mail;
        this.phone = phone;
        this.balance = balance;
    }

}
