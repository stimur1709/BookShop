package com.example.mybookshopapp.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class MailStructure {

    private String subject;
    private Map<String, Object> body;

    public MailStructure(String subject, Map<String, Object> body) {
        this.subject = subject;
        this.body = body;
    }


}
