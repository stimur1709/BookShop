package com.example.mybookshopapp.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "message")
public class Message extends Models {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "DATE")
    private Date time;

    @Column(name = "user_id")
    private int userId;

    @Column(columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String subject;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;
}
