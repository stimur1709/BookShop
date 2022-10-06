package com.example.mybookshopapp.service.userService;

import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.ContactConfirmationResponse;
import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserRepository;
import com.example.mybookshopapp.service.Book2UserTypeService;
import com.example.mybookshopapp.service.UserContactService;
import com.example.mybookshopapp.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserChangeService extends UserService {

    @Autowired
    public UserChangeService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                             Book2UserTypeService book2UserTypeService, UserContactService userContactService, Generator generator) {
        super(userRepository, passwordEncoder, book2UserTypeService, userContactService, generator);
    }

    public ContactConfirmationResponse handlerRequestChangeContactConfirmation(ContactConfirmationPayload payload) {
        UserContact userOldContact = userContactService.getUserContact(payload.getOldContact());
        if (userContactService.checkUserExistsByContact(payload.getContact()).isPresent()) {
            UserContact userNewContact = userContactService.getUserContact(payload.getContact());

            if (userNewContact.getApproved() == (short) 1) {
                String error = userOldContact.getType().equals(ContactType.PHONE)
                        ? "Указанный номер телефона уже привязан к другому пользователю, введите другой"
                        : "Указанная почта уже привязана к другому пользователю, введите другую";
                return new ContactConfirmationResponse(false, error);
            }
        }

        Thread thread = new Thread(() -> {
            try {
                wait(userOldContact, payload.getContact());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();

        return new ContactConfirmationResponse(true);
    }

    private void wait(UserContact userOldContact, String contact) throws InterruptedException {
        synchronized (this) {
            wait();
            userOldContact.setContact(contact);
            userContactService.changeContact(userOldContact);
            System.out.println(userOldContact + " save!!!!");
        }
    }

    private void notify(String q) {
        synchronized (this) {
            notify();
        }
    }

    public ContactConfirmationResponse handlerApproveContact(ContactConfirmationPayload payload) {
        UserContact userContact = new UserContact(payload.getContactType(), payload.getContact(), payload.getCode());
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

    private ContactConfirmationResponse blockContact(long time) {
        return new ContactConfirmationResponse(false,
                generator.generatorTextBlockContact(time, "Число попыток подтверждения превышено, повторите попытку через "));
    }

    private ContactConfirmationResponse badContact(int result, ContactType type) {
        ContactConfirmationResponse response = new ContactConfirmationResponse(true);
        response.setError(generator.generatorTextBadContact(type, result));
        return response;
    }
}

