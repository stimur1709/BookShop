package com.example.mybookshopapp.model.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user_login_history")
public class UserLoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(40) NOT NULL")
    private String osName;

    @Column(columnDefinition = "VARCHAR(40) NOT NULL")
    private String ipAddress;

    @Column(columnDefinition = "DATE NOT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", columnDefinition = "INT")
    private User user;

    public UserLoginHistory() {

    }
}
