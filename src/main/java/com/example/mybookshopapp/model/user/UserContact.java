package com.example.mybookshopapp.model.user;

import com.example.mybookshopapp.model.enums.ContactType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user_contact")
@Schema(description = "Сущность контакта пользователя")
public class UserContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "user_id", columnDefinition = "INT")
    @JsonBackReference
    private User user;

    @OneToOne(cascade = CascadeType.MERGE)
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

    public UserContact(User user, ContactType type, String code, String contact) {
        this.type = type;
        this.code = code;
        this.contact = contact;
        this.codeTime = new Date();
        this.user = user;
        this.code = code;
    }

    @Override
    public String toString() {
        return "UserContact{" +
                "id=" + id +
                ", user=" + user +
                ", type=" + type +
                ", approved=" + approved +
                ", code='" + code + '\'' +
                ", codeTrails=" + codeTrails +
                ", codeTime=" + codeTime +
                ", contact='" + contact + '\'' +
                '}';
    }
}
