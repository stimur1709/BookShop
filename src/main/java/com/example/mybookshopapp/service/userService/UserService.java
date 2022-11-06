package com.example.mybookshopapp.service.userService;

import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.ContactConfirmationResponse;
import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserRepository;
import com.example.mybookshopapp.service.Book2UserTypeService;
import com.example.mybookshopapp.service.UserContactService;
import com.example.mybookshopapp.util.Generator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    protected final UserRepository userRepository;
    protected final PasswordEncoder passwordEncoder;
    protected final Book2UserTypeService book2UserTypeService;
    protected final UserContactService userContactService;
    protected final Generator generator;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       Book2UserTypeService book2UserTypeService, UserContactService userContactService, Generator generator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.book2UserTypeService = book2UserTypeService;
        this.userContactService = userContactService;
        this.generator = generator;
    }

    public ContactConfirmationResponse handlerApproveContact(ContactConfirmationPayload payload, UserContact userContact) {
        long dif = Math.abs(userContact.getCodeTime().getTime() - new Date().getTime());

        if (userContact.getCodeTrails() > 2 && dif < 300000) {
            return blockContact(dif);
        }

        if (dif > 1000000) {
            return new ContactConfirmationResponse(false, "Код подтверждения устарел. Запросите новый");
        }

        if (!userContact.getCode().equals(payload.getCode())) {
            userContact.setCodeTrails(userContact.getCodeTrails() + 1);
            userContactService.save(userContact);
            return badContact(userContact.getCodeTrails(), userContact.getType());
        }

        userContact.setApproved((short) 1);
        userContactService.save(userContact);
        return new ContactConfirmationResponse(true);
    }

    protected ContactConfirmationResponse blockContact(long time) {
        return new ContactConfirmationResponse(false,
                generator.generatorTextBlockContact(time, "Число попыток подтверждения превышено, повторите попытку через "));
    }

    protected ContactConfirmationResponse badContact(int result, ContactType type) {
        ContactConfirmationResponse response = new ContactConfirmationResponse(true);
        response.setError(generator.generatorTextBadContact(type, result));
        return response;
    }

}
