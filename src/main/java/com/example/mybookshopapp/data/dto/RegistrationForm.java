package com.example.mybookshopapp.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationForm {

    private String firstname;
    private String lastname;
    private String mail;
    private String phone;
    private String password;

    public RegistrationForm() {
    }
}
