package com.example.mybookshopapp.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Image extends Models {

    private String name;
    private long size;

    public Image(Integer id) {
        super(id);
    }
}
