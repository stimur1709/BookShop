package com.example.mybookshopapp.data.outher.litres;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person {

    private int id;
    private String uuid;
    @JsonProperty("full_name")
    private String fullName;
    private String url;
    private String role;
}
