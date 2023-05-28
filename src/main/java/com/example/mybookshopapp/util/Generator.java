package com.example.mybookshopapp.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Random;

@Component
@Slf4j
public class Generator {

    private final Random random;
    private final MessageLocale messageLocale;

    @Autowired
    public Generator(Random random, MessageLocale messageLocale) {
        this.random = random;
        this.messageLocale = messageLocale;
    }

    public String getSecretCode() {
        String code = 100 + random.nextInt(999 - 100 + 1) + " " + (100 + random.nextInt(999 - 100 + 1));
        log.info("Generation code " + code);
        return code;
    }

    public String generatorTextBlockContact(long time, String text) {
        long value;
        String result;
        if (time > 240000) {
            value = (300000 - time) / 1000;
            result = ' ' + getSecond(value);
        } else {
            value = 5 - time / 60000;
            result = ' ' + getMinute(value);
        }
        return text + value + result;
    }

    public String generatorTextBadContact(int result) {
        int count = 3 - result;
        String password = messageLocale.getMessage("message.password");
        String enteredIncorrectly = ' ' + messageLocale.getMessage("message.enteredIncorrectly");
        String attempt = ' ' + messageLocale.getMessage("message.attempt");
        String attempts = ' ' + messageLocale.getMessage("message.attempts");
        return count == 1 ? password + enteredIncorrectly + count + attempt
                : password + enteredIncorrectly + count + attempts;
    }

    public String generateHashCode() {
        StringBuilder result = new StringBuilder();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz" .toCharArray();
        for (int i = 0; i <= 18; i++) {
            result.append(alphabet[random.nextInt(alphabet.length)]);
        }
        return result.toString();
    }

    private String getSecond(long value) {
        if (Arrays.asList(1, 21, 31, 41, 51).contains((int) value)) {
            return messageLocale.getMessage("message.second");
        } else if (Arrays.asList(2, 3, 4, 22, 23, 24, 32, 33, 34, 42, 43, 44, 52, 53, 54).contains((int) value)) {
            return messageLocale.getMessage("message.second1");
        } else {
            return messageLocale.getMessage("message.seconds2");
        }
    }

    private String getMinute(long value) {
        if (value == 1) {
            return messageLocale.getMessage("message.minute");
        } else if (value == 5) {
            return messageLocale.getMessage("message.minute1");
        } else {
            return messageLocale.getMessage("message.minute2");
        }

    }

}
