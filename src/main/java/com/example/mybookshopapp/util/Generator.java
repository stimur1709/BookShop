package com.example.mybookshopapp.util;

import com.example.mybookshopapp.model.enums.ContactType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Generator {

    private final Random random;

    @Autowired
    public Generator(Random random) {
        this.random = random;
    }

    public String getSecretCode() {
        String code = 100 + random.nextInt(999 - 100 + 1) + " " + (100 + random.nextInt(999 - 100 + 1));
        System.out.println(code);
        return code;
    }

    public String generatorTextBlockContact(long time, String text) {
        long value;
        String result;
        if (time > 240000) {
            value = (300000 - time) / 1000;
            result = value == 1 || value == 21 || value == 31 || value == 41 || value == 51 ? " секунду"
                    : value == 2 || value == 3 || value == 4 || value == 22 || value == 23 || value == 24 || value == 32 || value == 33 || value == 34 || value == 42 || value == 43 || value == 44 || value == 52 || value == 53 || value == 54 ? " секунды"
                    : " секунд";
        } else {
            value = 5 - time / 60000;
            result = value == 1 ? " минуту" : value == 5 ? " минут" : " минуты";
        }

        return text + value + result;
    }

    public String generatorTextBadContact(ContactType type, int result) {
        int count = 3 - result;
        String pass = type.equals(ContactType.PHONE) ? "Код подтверждения" : "Пароль";
        return count == 1 ? pass + " введён неверно. У вас осталось " + count + " попытка"
                : pass + " введён неверно. У вас осталось " + count + " попытки";
    }
}
