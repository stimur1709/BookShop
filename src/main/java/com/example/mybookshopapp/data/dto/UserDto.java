package com.example.mybookshopapp.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends Dto {

    private String firstname;
    private String lastname;
    private String mail;
    private int approvedMail;
    private String phone;
    private int approvedPhone;
    private int balance;

    public UserDto(String firstname, String lastname, String mail, int approvedMail, String phone, int approvedPhone, int balance) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.mail = mail;
        this.approvedMail = approvedMail;
        this.phone = phone;
        this.approvedPhone = approvedPhone;
        this.balance = balance;
    }

}
