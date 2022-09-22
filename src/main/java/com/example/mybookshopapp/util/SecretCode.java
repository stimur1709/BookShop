package com.example.mybookshopapp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SecretCode {

    private final Random random;

    @Autowired
    public SecretCode(Random random) {
        this.random = random;
    }

    public String getSecretCode() {
        String code = 100 + random.nextInt(999 - 100 + 1) + " " + (100 + random.nextInt(999 - 100 + 1));
        System.out.println(code);
        return code;
    }
}
