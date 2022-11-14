package com.example.mybookshopapp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegistrationForm {

    private String firstname;
    private String lastname;
    private String mail;
    private String phone;
    private String password;
}
