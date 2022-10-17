package com.example.mybookshopapp.service.userService;

import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserRepository;
import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.ContactConfirmationResponse;
import com.example.mybookshopapp.dto.RegistrationForm;
import com.example.mybookshopapp.service.Book2UserTypeService;
import com.example.mybookshopapp.service.UserContactService;
import com.example.mybookshopapp.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserRegisterService extends UserService {


    @Autowired
    public UserRegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                               Book2UserTypeService book2UserTypeService, UserContactService userContactService,
                               Generator generator) {
        super(userRepository, passwordEncoder, book2UserTypeService, userContactService, generator);
    }

    public void registerUser(RegistrationForm registrationForm, String cartContent, String keptContent) {
        User user = new User(registrationForm.getName(),
                passwordEncoder.encode(registrationForm.getPassword()));
        UserContact contactEmail = userContactService.getUserContact(registrationForm.getEmail());
        UserContact contactPhone = userContactService.getUserContact(registrationForm.getPhone());

        contactEmail.setUser(user);
        contactPhone.setUser(user);

        user.getUserContact().add(contactEmail);
        user.getUserContact().add(contactPhone);

        userRepository.save(user);
        userContactService.save(contactEmail);
        userContactService.save(contactPhone);

        book2UserTypeService.addBooksTypeUserFromCookie(cartContent, keptContent, user);
    }

    public ContactConfirmationResponse handlerRequestNewContactConfirmation(ContactConfirmationPayload payload) {
        UserContact userContact = userContactService.getUserContact(payload.getContact());
        if (userContact != null && userContact.getApproved() == (short) 1) {
            String error = userContact.getType().equals(ContactType.PHONE)
                    ? "Указанный номер телефона уже привязан к другому пользователю, введите другой"
                    : "Указанная почта уже привязана к другому пользователю, введите другую";
            return new ContactConfirmationResponse(false, error);
        }
        if (userContact != null && userContact.getApproved() == (short) 0) {

            long dif = Math.abs(userContact.getCodeTime().getTime() - new Date().getTime());

            if (userContact.getCodeTrails() >= 2 && dif < 300000) {
                return blockContact(dif);
            }

            userContactService.changeContact(userContact);

        } else {
            UserContact contact = new UserContact(payload.getContactType(), payload.getContact(), generator.getSecretCode());
            userContactService.save(contact);

        }
        return new ContactConfirmationResponse(true);
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