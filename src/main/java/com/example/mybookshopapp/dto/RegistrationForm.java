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

    public RegistrationForm(String firstname, String lastname, String mail, String phone, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.mail = mail;
        this.phone = phone;
        this.password = password;
    }

    public RegistrationForm() {
    }
}
