package com.example.mybookshopapp.util;

import com.example.mybookshopapp.data.outher.ChangeProfileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FormValidator implements Validator {

    private final MessageLocale messageLocale;

    @Autowired
    public FormValidator(MessageLocale messageLocale) {
        this.messageLocale = messageLocale;
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
                String messagePass5and10 = messageLocale.getMessage("message.password5and100");
                errors.rejectValue("password", "", messagePass5and10);
            }
            if (!changeProfileForm.getPassword().equals(changeProfileForm.getPasswordRepeat())) {
                String passwordRepeat = messageLocale.getMessage("message.passwordRepeat");
                errors.rejectValue("passwordRepeat", "", passwordRepeat);
            }
        }
    }
}
