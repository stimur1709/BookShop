package com.example.mybookshopapp.model.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.SimpleDateFormat;
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
    private String system;

    @Column(columnDefinition = "VARCHAR(40) NOT NULL")
    private String ipAddress;

    @Column(columnDefinition = "VARCHAR(50)")
    private String city;

    @Column(columnDefinition = "VARCHAR(50)")
    private String country;

    @Column(columnDefinition = "DATE NOT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", columnDefinition = "INT")
    @JsonBackReference
    private User user;

    @Transient
    private String formatDate;

    public UserLoginHistory() {
    }

    public UserLoginHistory(String system, String ipAddress, String city, String country, User user) {
        this.system = system;
        this.ipAddress = ipAddress;
        this.user = user;
        this.city = city;
        this.country = country;
        this.date = new Date();
    }

    public void setFormatDate(SimpleDateFormat simpleDateFormat) {
        this.formatDate = simpleDateFormat.format(date);
    }
}
