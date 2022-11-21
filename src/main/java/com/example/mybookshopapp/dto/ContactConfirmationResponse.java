package com.example.mybookshopapp.dto;

import com.example.mybookshopapp.model.enums.ContactType;
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
    private ContactType type;

    public ContactConfirmationResponse(boolean result) {
        this.result = result;
    }
    public ContactConfirmationResponse(boolean result, ContactType type) {
        this.result = result;
        this.type = type;
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
