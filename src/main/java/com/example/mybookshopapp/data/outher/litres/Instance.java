package com.example.mybookshopapp.data.outher.litres;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Instance {

    private int id;
    private String uuid;
    @JsonProperty("cover_url")
    private String coverUrl;
    private String url;
    private String title;
    private String subtitle;
    private int availability;
    private ArrayList<Person> persons;
    private ArrayList<Object> series;
    private Labels labels;
}
