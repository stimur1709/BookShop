package com.example.mybookshopapp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContactConfirmationResponse {

    private boolean result;
    private String token;
    private String error;

    public ContactConfirmationResponse(boolean result) {
        this.result = result;
    }

    public ContactConfirmationResponse(boolean result, String text) {
        this.result = result;
        if (result)
            this.token = text;
        else
            this.error = text;
    }

    public ContactConfirmationResponse() {
    }
}
