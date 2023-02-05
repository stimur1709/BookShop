package com.example.mybookshopapp.data.entity.config;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table
@Entity
@Getter
@Setter
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "varchar(255) not null")
    private String username;

    @Column(columnDefinition = "varchar(255) not null")
    private String password;

    @Column(columnDefinition = "varchar(255) not null")
    private String host;

    @Column(columnDefinition = "varchar(255) not null")
    private String protocol;

    @Column(columnDefinition = "SMALLINT NOT NULL")
    private int port;

    @Column(name = "IS_MAIN", columnDefinition = "boolean not null default value false")
    private boolean isMain;

    @Column(name = "SMMTP_AUTH", columnDefinition = "boolean not null default value true")
    private boolean smtpAuth;

    @Column(name = "SMMTP_STARTTTLS_ENABLE", columnDefinition = "boolean not null default value false")
    private boolean smtpStarttlsEnable;

    @Column(name = "SMMTP_SSL_ENABLE", columnDefinition = "boolean not null default value false")
    private boolean smtpSslEnable;

    @Column(columnDefinition = "boolean not null default value false")
    private boolean debug;

}
