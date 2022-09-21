package com.example.mybookshopapp.util;

import com.example.mybookshopapp.security.model.ContactConfirmationPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BadAuthCounterValidator implements Validator {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public BadAuthCounterValidator(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return ContactConfirmationPayload.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ContactConfirmationPayload payload = (ContactConfirmationPayload) target;
         if(!authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                 payload.getCode())).isAuthenticated())
             errors.rejectValue("result", "", "Код подтверждения введён неверно. У вас осталось N попыток");
    }
}
