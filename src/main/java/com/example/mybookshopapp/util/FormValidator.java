package com.example.mybookshopapp.util;

import com.example.mybookshopapp.dto.ChangeProfileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;

@Component
public class FormValidator implements Validator {

    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;
    private final HttpServletRequest request;

    @Autowired
    public FormValidator(MessageSource messageSource, LocaleResolver localeResolver, HttpServletRequest request) {
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
        this.request = request;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return ChangeProfileForm.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        ChangeProfileForm changeProfileForm = (ChangeProfileForm) target;
        if (!changeProfileForm.getPassword().isEmpty()) {
            if (changeProfileForm.getPassword().length() < 5 || changeProfileForm.getPassword().length() > 100) {
                String messagePass5and10 = messageSource.getMessage("message.password5and100", null, localeResolver.resolveLocale(request));
                errors.rejectValue("password", "", messagePass5and10);
            }
            if (!changeProfileForm.getPassword().equals(changeProfileForm.getPasswordRepeat())) {
                String passwordRepeat = messageSource.getMessage("message.passwordRepeat", null, localeResolver.resolveLocale(request));
                errors.rejectValue("passwordRepeat", "", passwordRepeat);
            }
        }
    }
}
