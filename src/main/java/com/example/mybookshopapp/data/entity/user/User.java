package com.example.mybookshopapp.data.entity.user;

import com.example.mybookshopapp.data.entity.Models;
import com.example.mybookshopapp.data.entity.payments.BalanceTransaction;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@Schema(description = "Сущность пользователя")
public class User extends Models {

    @Column(columnDefinition = "VARCHAR(32) NOT NULL", unique = true)
    private String hash;

    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "DATE")
    private Date regTime;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int balance;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String firstname;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String lastname;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REFRESH)
    @JsonManagedReference
    private List<UserContact> userContact = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BalanceTransaction> transactionList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<UserLoginHistory> userLoginHistories;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user2Role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<UserRole> userRoles;

    public User(String firstname, String lastname, String password, String hash) {
        this.hash = hash;
        this.password = password;
        this.regTime = new Date();
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User(String firstname, String lastname, String hash) {
        this.hash = hash;
        this.regTime = new Date();
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User() {
    }
}
