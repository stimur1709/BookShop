package com.example.mybookshopapp.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Image extends Models {

    private String name;
    private long size;

    public Image(Integer id) {
        super(id);
    }

    public Image(String name, long size) {
        this.name = name;
        this.size = size;
    }
}
