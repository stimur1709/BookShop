package com.example.mybookshopapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseResultDto {

    private Boolean result;
    private String error;

    public ResponseResultDto(Boolean result) {
        this.result = result;
    }

    public ResponseResultDto(Boolean result, String error) {
        this.result = result;
        this.error = error;
    }
}
