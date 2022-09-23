package com.example.mybookshopapp.security.model;

import com.example.mybookshopapp.model.enums.ContactType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactConfirmationPayload {

    private String contact;
    private ContactType contactType;
    private String code;
    private String oldContact;
}
