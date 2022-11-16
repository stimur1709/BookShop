package com.example.mybookshopapp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class ChangeProfileForm {

    @Size(min = 2, max = 100, message = "{message.firstnameEmpty}")
    private String firstname;

    @Size(min = 2, max = 100, message = "{message.lastnameEmpty}")
    private String lastname;

    @NotEmpty(message = "{message.validMail}")
    @Email(message = "{message.validMail}")
    private String email;
    private String oldEmail;
    private int approvedMail;

    @Pattern(regexp = "^\\+7 \\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}", message = "{message.validPhone}")
    private String phone;
    private String oldPhone;
    private int approvedPhone;
    private String password;
    private String passwordRepeat;

}
