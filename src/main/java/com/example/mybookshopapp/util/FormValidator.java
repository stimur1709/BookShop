package com.example.mybookshopapp.util;

import com.example.mybookshopapp.dto.ChangeProfileForm;
import com.example.mybookshopapp.service.UserContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FormValidator implements Validator {

    private final UserContactService userContactService;

    @Autowired
    public FormValidator(UserContactService userContactService) {
        this.userContactService = userContactService;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return ChangeProfileForm.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        ChangeProfileForm changeProfileForm = (ChangeProfileForm) target;
        if (!changeProfileForm.getEmail().equals(changeProfileForm.getOldEmail())) {
            if (userContactService.checkUserExistsByContact(changeProfileForm.getEmail()).isPresent())
                errors.rejectValue("email", "", "Указанная почта уже привязана к другому пользователю, введите другую");
        }
        if (!changeProfileForm.getPhone().equals(changeProfileForm.getOldPhone())) {
            if (userContactService.checkUserExistsByContact(changeProfileForm.getPhone()).isPresent())
                errors.rejectValue("phone", "", "Указанный номер телефона уже привязан к другому пользователю, введите другой");
        }
        if (!changeProfileForm.getPassword().isEmpty()) {
            if (changeProfileForm.getPassword().length() < 5 || changeProfileForm.getPassword().length() > 100)
                errors.rejectValue("password", "", "Пароль должен быть от 5 до 100 символов длиной");
            if (!changeProfileForm.getPassword().equals(changeProfileForm.getPasswordRepeat()))
                errors.rejectValue("passwordRepeat", "", "Пароли не совпадают");
        }
    }
}
