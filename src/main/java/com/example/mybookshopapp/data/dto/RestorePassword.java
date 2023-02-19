package com.example.mybookshopapp.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestorePassword {

    private String password;
    private String code;

    public RestorePassword() {
    }
}
