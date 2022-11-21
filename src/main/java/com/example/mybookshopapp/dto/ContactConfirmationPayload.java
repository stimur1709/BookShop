package com.example.mybookshopapp.dto;

import com.example.mybookshopapp.model.enums.ContactType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContactConfirmationPayload {

    private String contact;
    private ContactType contactType;
    private String code;
    private String oldContact;

}
