package com.example.mybookshopapp.data.entity.book.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "message")
@Schema(description = "Сообщения в форму обратной связи")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    public Message(int userId, String email, String name, String subject, String text) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.subject = subject;
        this.text = text;
        this.time = new Date();
    }

    public Message() {

    }
}
