package com.example.mybookshopapp.model.user;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserLoginHistory {

    private String osName;
    private String ipAddress;
    private Date date;

    private User user;

    public UserLoginHistory(String osName, String ipAddress, User user) {
        this.osName = osName;
        this.ipAddress = ipAddress;
        this.date = new Date();
        this.user = user;
    }
}
