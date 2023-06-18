package com.example.mybookshopapp.data.outher;

import com.example.mybookshopapp.data.entity.enums.ContactType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ContactConfirmationResponse {

    private List<String> roles;
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
        if (result) {
            this.token = text;
        } else {
            this.error = text;
        }
    }

    public ContactConfirmationResponse(boolean result, String text, List<String> roles) {
        this.result = result;
        this.token = text;
        this.roles = roles;
    }

    public ContactConfirmationResponse() {
    }
}
