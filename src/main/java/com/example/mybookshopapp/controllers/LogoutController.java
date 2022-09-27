package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.config.UserRequestScopedBean;
import com.example.mybookshopapp.security.service.BlackListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    private final BlackListingService blackListingService;
    private final UserRequestScopedBean userRequestScopedBean;

    @Autowired
    public LogoutController(BlackListingService blackListingService, UserRequestScopedBean userRequestScopedBean) {
        this.blackListingService = blackListingService;
        this.userRequestScopedBean = userRequestScopedBean;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        System.out.println("3212");
        blackListingService.blackListJwt(userRequestScopedBean.getJwt());
        return ResponseEntity.ok(null);
    }
}
