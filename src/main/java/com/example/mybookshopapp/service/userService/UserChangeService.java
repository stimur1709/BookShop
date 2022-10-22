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

        return new ContactConfirmationResponse(true);
    }

    public ContactConfirmationResponse handlerApproveContact(ContactConfirmationPayload payload) {
        UserContact userContact = new UserContact(payload.getContactType(), payload.getContact(), payload.getCode());
        return super.handlerApproveContact(payload, userContact);
    }
}

