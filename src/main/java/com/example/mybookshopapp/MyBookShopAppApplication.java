package com.example.mybookshopapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
}
