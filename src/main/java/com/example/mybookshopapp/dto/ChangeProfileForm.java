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

    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов длиной")
    private String firstname;

    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов длиной")
    private String lastname;

    @NotEmpty(message = "Email не должен быть пустым")
    @Email(message = "Email должен быть в формате *****@***.**")
    private String email;
    private String oldEmail;

    @NotEmpty(message = "Номер телефона не может быть пустым")
    @Pattern(regexp = "^\\+7 \\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}", message = "Телефон должен быть в формате +7 (xxx) xxx-xx-xx")
    private String phone;
    private String oldPhone;
    private String password;
    private String passwordRepeat;

}
