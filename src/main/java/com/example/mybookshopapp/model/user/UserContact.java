package com.example.mybookshopapp.model.user;

import com.example.mybookshopapp.model.enums.ContactType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_contact")
@Schema(description = "Сущность контакта пользователя")
public class UserContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ContactType type;

    @Column(columnDefinition = "SMALLINT NOT NULL DEFAULT 0")
    private short approved;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String code;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int codeTrails;

    @Column(columnDefinition = "DATE")
    private LocalDateTime codeTime;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String contact;

    public UserContactEntity(UserEntity user, ContactType type, String contact) {
        this.code = "1234";
        this.user = user;
        this.type = type;
        this.contact = contact;
    }

    public UserContactEntity() {
    }
}
