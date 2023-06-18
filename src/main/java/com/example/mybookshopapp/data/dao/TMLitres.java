package com.example.mybookshopapp.data.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TMLitres {

    private int id;
    private String searchName;
    private int searchId;
    private int lastStart;
    private long total;
    private boolean active;
    private short type;
}
