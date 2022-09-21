package com.example.mybookshopapp.security.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    @Override
    public String toString() {
        return "ContactConfirmationResponse{" +
                "result=" + result +
                ", token='" + token + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
