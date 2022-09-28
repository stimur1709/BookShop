package com.example.mybookshopapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {


    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        System.out.println("3212");
        return ResponseEntity.ok(null);
    }
}
