package com.example.mybookshopapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;

import java.util.Random;

@SpringBootApplication
public class MyBookShopAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBookShopAppApplication.class, args);
    }

    @Bean
    public Random getRandom() {
        return new Random();
    }

    @Bean
    public OAuth2UserRequestEntityConverter getOAuth2UserRequestEntityConverter() {
        return new OAuth2UserRequestEntityConverter();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
