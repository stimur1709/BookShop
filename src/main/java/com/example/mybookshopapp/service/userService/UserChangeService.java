package com.example.mybookshopapp.service.userService;

import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.ContactConfirmationResponse;
import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.service.UserContactService;
import com.example.mybookshopapp.service.UserProfileService;
import com.example.mybookshopapp.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserChangeService {

    private final UserProfileService userProfileService;
    private final UserContactService userContactService;
    private final Generator generator;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserChangeService(UserProfileService userProfileService, UserContactService userContactService,
                             Generator generator, PasswordEncoder passwordEncoder) {
        this.userProfileService = userProfileService;
        this.userContactService = userContactService;
        this.generator = generator;
        this.passwordEncoder = passwordEncoder;
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
            userNewContact = new UserContact(userOldContact.getUser(), userOldContact.getParentUserContact(),
                    payload.getContactType(), passwordEncoder.encode(generator.getSecretCode()), payload.getContact());
            userNewContact.setParentUserContact(userOldContact);
            userContactService.save(userNewContact);
            userContactService.save(userOldContact);

        } else {
            userNewContact = new UserContact(userProfileService.getCurrentUser(),
                    payload.getContactType(), generator.getSecretCode(), payload.getContact());
            userContactService.save(userNewContact);
        }

        return new ContactConfirmationResponse(true);
    }
}

