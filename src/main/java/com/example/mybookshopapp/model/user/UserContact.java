package com.example.mybookshopapp.model.user;

import com.example.mybookshopapp.model.enums.ContactType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user_contact")
@Schema(description = "Сущность контакта пользователя")
@ToString
public class UserContact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", columnDefinition = "INT")
    @JsonBackReference
    private User user;

    @OneToOne()
    @JoinColumn(name = "parent_id", columnDefinition = "INT")
    private UserContact parentUserContact;

    @Enumerated(EnumType.STRING)
    private ContactType type;

    @Column(columnDefinition = "SMALLINT NOT NULL DEFAULT 0")
    private short approved;

    @Column(columnDefinition = "VARCHAR(255)")
    private String code;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int codeTrails;

    @Temporal(TemporalType.TIMESTAMP)
    private Date codeTime;

    @Column(unique = true, columnDefinition = "VARCHAR(255) NOT NULL")
    private String contact;

    @OneToOne(mappedBy = "parentUserContact")
    private UserContact childUserContact;

    public UserContact(ContactType type, String contact, String code) {
        this.type = type;
        this.code = code;
        this.contact = contact;
        this.codeTime = new Date();
    }

    public UserContact(ContactType type, String contact) {
        this.type = type;
        this.approved = 1;
        this.contact = contact;
        this.codeTime = new Date();
    }

    public UserContact(ContactType type, String contact, UserContact userContact) {
        this.type = type;
        this.approved = 1;
        this.contact = contact;
        this.codeTime = new Date();
        this.parentUserContact = userContact;
    }

    public UserContact() {
    }

    public UserContact(User user, UserContact userContact, ContactType type, String code, String contact) {
        this.type = type;
        this.code = code;
        this.contact = contact;
        this.codeTime = new Date();
        this.user = user;
        this.parentUserContact = userContact;
        this.code = code;
    }

    public UserContact(User user, ContactType contactType, String contact) {
        this.user = user;
        this.type = contactType;
        this.contact = contact;
    }

}
