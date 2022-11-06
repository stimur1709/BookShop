package com.example.mybookshopapp.util;

import com.example.mybookshopapp.dto.RegistrationForm;
import com.example.mybookshopapp.service.UserContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegFormValidator implements Validator {

    private final UserContactService userContactService;

    @Autowired
    public RegFormValidator(UserContactService userContactService) {
        this.userContactService = userContactService;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return RegistrationForm.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        RegistrationForm registrationForm = (RegistrationForm) target;
        if (userContactService.checkUserExistsByContact(registrationForm.getMail()).isPresent())
            errors.rejectValue("email", "", "Пользователь с таким email уже существует");
        if (userContactService.checkUserExistsByContact(registrationForm.getPhone()).isPresent())
            errors.rejectValue("phone", "", "Пользователь с таким номером" +
                    " телефона уже существует");
    }
}
