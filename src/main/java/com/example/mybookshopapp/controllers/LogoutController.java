package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
public class LogoutController {

    private final BlacklistService blacklistService;

    @Autowired
    public LogoutController(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        System.out.println("3212");
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("token")) {
                blacklistService.add(cookie);
            }
        }

        return ResponseEntity.ok(null);
    }
}
