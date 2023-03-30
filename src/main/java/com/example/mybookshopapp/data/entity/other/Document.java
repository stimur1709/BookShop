package com.example.mybookshopapp.data.entity.other;

import com.example.mybookshopapp.data.entity.Models;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "document")
@Getter
@Setter
public class Document extends Models {

    @Column(columnDefinition = "INT NOT NULL  DEFAULT 0")
    private int sortIndex;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String title;

    @Column(name = "html_text", columnDefinition = "TEXT NOT NULL")
    private String htmlText;

    private String description;

    private String image;

}
