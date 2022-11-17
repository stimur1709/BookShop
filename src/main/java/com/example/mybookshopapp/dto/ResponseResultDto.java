package com.example.mybookshopapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseResultDto {

    private Boolean result;
    private String error;
    private String name;
    private String text;
    private String date;

    public ResponseResultDto(Boolean result) {
        this.result = result;
    }
    public ResponseResultDto(Boolean result, String error) {
        this.result = result;
        this.error = error;
    }

    public ResponseResultDto(Boolean result, String text, String name, String date) {
        this.result = result;
        this.text = text;
        this.name = name;
        this.date = date;
    }
}
