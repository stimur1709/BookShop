package com.example.mybookshopapp.data.outher;

import com.example.mybookshopapp.data.entity.enums.ContactType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactConfirmationPayload {

    private String contact;
    private ContactType contactType;
    private String code;
    private String oldContact;

    public ContactConfirmationPayload(String contact, ContactType contactType) {
        this.contact = contact;
        this.contactType = contactType;
    }

    public ContactConfirmationPayload() {
    }

}
