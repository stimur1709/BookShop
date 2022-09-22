package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserRepository;
import com.example.mybookshopapp.security.model.ContactConfirmationPayload;
import com.example.mybookshopapp.security.model.ContactConfirmationResponse;
import com.example.mybookshopapp.security.model.RegistrationForm;
import com.example.mybookshopapp.util.SecretCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserRegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Book2UserTypeService book2UserTypeService;
    private final UserContactService userContactService;
    private final SecretCode secretCode;

    @Autowired
    public UserRegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                               Book2UserTypeService book2UserTypeService,
                               UserContactService userContactService, SecretCode secretCode) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.book2UserTypeService = book2UserTypeService;
        this.userContactService = userContactService;
        this.secretCode = secretCode;
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
            userContact.setCode(secretCode.getSecretCode());
            userContact.setCodeTime(new Date());
            userContact.setCodeTrails(0);
            userContactService.save(userContact);

        } else {
            UserContact contact = new UserContact(payload.getContactType(), payload.getContact(), secretCode.getSecretCode());
            userContactService.save(contact);

        }
        return new ContactConfirmationResponse(true);
    }

    public ContactConfirmationResponse handlerApproveContact(ContactConfirmationPayload payload) {
        UserContact userContact = userContactService.getUserContact(payload.getContact());
        if (userContact.getCodeTrails() >= 2)
            return blockContact();
        if (!userContact.getCode().equals(payload.getCode())) {
            userContact.setCodeTrails(userContact.getCodeTrails() + 1);
            userContactService.save(userContact);
            return badContact(userContact.getCodeTrails(), userContact.getType());
        }

        userContact.setApproved((short) 1);
        userContactService.save(userContact);
        return new ContactConfirmationResponse(true);
    }

    private ContactConfirmationResponse blockContact() {
//        ContactConfirmationResponse response = ;
//        response.setError("Число попыток подтверждения превышено, повторите попытку через 5 минут");
        return new ContactConfirmationResponse(false, "Число попыток подтверждения превышено, повторите попытку через 5 минут");
    }

    private ContactConfirmationResponse badContact(int result, ContactType type) {
        int count = 3 - result;
        ContactConfirmationResponse response = new ContactConfirmationResponse(true);
        String pass = type.equals(ContactType.PHONE) ? "Код подтверждения" : "Пароль";
        String text = count == 1 ? pass + " введён неверно. У вас осталось " + count + " попытка"
                : pass + " введён неверно. У вас осталось " + count + " попытки";
        response.setError(text);
        return response;
    }
}