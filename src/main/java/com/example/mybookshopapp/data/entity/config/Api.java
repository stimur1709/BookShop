package com.example.mybookshopapp.data.entity.config;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
public class Api {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "varchar(255) not null")
    private String name;

    @Column(columnDefinition = "varchar(255) not null")
    private String url;

    @Column(columnDefinition = "varchar(255) not null")
    private String username;

    @Column(name = "api_key", columnDefinition = "varchar(255) not null")
    private String apiKey;

    @Column(columnDefinition = "varchar(255)")
    private String comment;

    @Column(columnDefinition = "varchar(255)")
    private String urlDocumentation;

    @Enumerated(EnumType.STRING)
    private Property property;

    @Column(name = "IS_MAIN", columnDefinition = "boolean not null default value false")
    private boolean isMain;

}
