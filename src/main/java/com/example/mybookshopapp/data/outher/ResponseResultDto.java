package com.example.mybookshopapp.data.outher;

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
        if (Boolean.TRUE.equals(result)) {
            this.text = text;
        } else {
            this.error = text;
        }
    }

    public ResponseResultDto(Boolean result, String name, int value, Map<Integer, Long> maps) {
        this.result = result;
        this.name = name;
        this.value = value;
        this.maps = maps;
    }

}
