package com.example.mybookshopapp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MessageLocale {

    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;
    private final HttpServletRequest request;

    @Autowired
    public MessageLocale(MessageSource messageSource, LocaleResolver localeResolver, HttpServletRequest request) {
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
        this.request = request;
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, localeResolver.resolveLocale(request));
    }

    public String getLocaleDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm", localeResolver.resolveLocale(request));
        return simpleDateFormat.format(date);
    }
}
