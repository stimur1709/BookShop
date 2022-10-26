package com.example.mybookshopapp.service.userService;

import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.ContactConfirmationResponse;
import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserRepository;
import com.example.mybookshopapp.service.Book2UserTypeService;
import com.example.mybookshopapp.service.UserContactService;
import com.example.mybookshopapp.service.UserProfileService;
import com.example.mybookshopapp.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserChangeService extends UserService {

    private final UserProfileService userProfileService;

    @Autowired
    public UserChangeService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                             Book2UserTypeService book2UserTypeService, UserContactService userContactService, Generator generator, UserProfileService userProfileService) {
        super(userRepository, passwordEncoder, book2UserTypeService, userContactService, generator);
        this.userProfileService = userProfileService;
    }

    public ContactConfirmationResponse handlerRequestChangeContactConfirmation(ContactConfirmationPayload payload) {
        Optional<UserContact> userContact = userContactService.checkUserExistsByContact(payload.getContact());
        if (userContact.isPresent()) {

            if (userContact.get().getApproved() == (short) 1) {
                String error = payload.getContactType().equals(ContactType.PHONE)
                        ? "Указанный номер телефона уже привязан к другому пользователю, введите другой"
                        : "Указанная почта уже привязана к другому пользователю, введите другую";
                return new ContactConfirmationResponse(false, error);
            }
        }
        UserContact userNewContact;
        if (payload.getOldContact() != null) {
            UserContact userOldContact = userContactService.getUserContact(payload.getOldContact());
            userNewContact = new UserContact(userOldContact.getUser(), userOldContact.getUserContact(),
                    payload.getContactType(), generator.getSecretCode(), payload.getContact());
            userOldContact.getUserContacts().add(userNewContact);
            userContactService.save(userOldContact);
            userContactService.save(userNewContact);

        } else {
            userNewContact = new UserContact(userProfileService.getCurrentUser(),
                    payload.getContactType(), generator.getSecretCode(), payload.getContact());
            userContactService.save(userNewContact);
        }


        //todo
        return new ContactConfirmationResponse(true);

    }

    public ContactConfirmationResponse handlerApproveContact(ContactConfirmationPayload payload) {
        UserContact userContact = userContactService.getUserContact(payload.getContact());
        return super.handlerApproveContact(payload, userContact);
    }
}

