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
    private String osName;

    @Column(columnDefinition = "VARCHAR(40) NOT NULL")
    private String ipAddress;

    @Column(columnDefinition = "DATE NOT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", columnDefinition = "INT")
    @JsonBackReference
    private User user;

    @Transient
    private String formatDate;

    public UserLoginHistory() {
    }

    public UserLoginHistory(String osName, String ipAddress, User user) {
        this.osName = osName;
        this.ipAddress = ipAddress;
        this.user = user;
        this.date = new Date();
    }

    public void setFormatDate(SimpleDateFormat simpleDateFormat) {
        this.formatDate = simpleDateFormat.format(date);
    }
}
