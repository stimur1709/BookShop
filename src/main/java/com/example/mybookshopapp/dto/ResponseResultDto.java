package com.example.mybookshopapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ResponseResultDto {

    private Boolean result;
    private String error;
    private String name;
    private String text;
    private String date;
    private int value;
    private Map<Integer, Long> maps;

    public ResponseResultDto(Boolean result) {
        this.result = result;
    }

    public ResponseResultDto(Boolean result, String text) {
        this.result = result;
        if (result)
            this.text = text;
        else
            this.error = text;
    }

    public ResponseResultDto(Boolean result, String name, int value, Map<Integer, Long> maps) {
        this.result = result;
        this.name = name;
        this.value = value;
        this.maps = maps;
    }

    public ResponseResultDto(Boolean result, String text, String name, String date) {
        this.result = result;
        this.text = text;
        this.name = name;
        this.date = date;
    }
}
