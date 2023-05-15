package com.example.mybookshopapp.data.daoEntity;

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
    private Boolean active;
    private short type;
}
