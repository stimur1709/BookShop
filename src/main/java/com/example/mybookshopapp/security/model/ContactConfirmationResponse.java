package com.example.mybookshopapp.security.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactConfirmationResponse {

    private String result;

    public ContactConfirmationResponse() {
    }

    public ContactConfirmationResponse(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ContactConfirmationResponse{" +
                "result='" + result + '\'' +
                '}';
    }
}
