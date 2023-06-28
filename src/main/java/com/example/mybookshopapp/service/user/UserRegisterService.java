package com.example.mybookshopapp.service.user;

import com.example.mybookshopapp.data.entity.enums.ContactType;
import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.data.entity.user.UserContact;
import com.example.mybookshopapp.data.outher.ContactConfirmationPayload;
import com.example.mybookshopapp.data.outher.ContactConfirmationResponse;
import com.example.mybookshopapp.data.outher.RegistrationForm;
import com.example.mybookshopapp.repository.UserRepository;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.UserContactService;
import com.example.mybookshopapp.util.Generator;
import com.example.mybookshopapp.util.MessageLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserRegisterService {

    private final UserContactService userContactService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final Generator generator;
    private final BookShopService bookShopService;
    private final UserProfileService userProfileService;
    private final MessageLocale messageLocale;

    @Autowired
    public UserRegisterService(UserContactService userContactService, PasswordEncoder passwordEncoder,
                               UserRepository userRepository, Generator generator,
                               BookShopService bookShopService, UserProfileService userProfileService,
                               MessageLocale messageLocale) {
        this.userContactService = userContactService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.generator = generator;
        this.bookShopService = bookShopService;
        this.userProfileService = userProfileService;
        this.messageLocale = messageLocale;
    }

    public User registerUser(RegistrationForm registrationForm) {
        int userOld = userProfileService.getUserId();
        User user = new User(registrationForm.getFirstname(), registrationForm.getLastname(),
                passwordEncoder.encode(registrationForm.getPassword()), generator.generateHashCode());
        UserContact contactEmail = userContactService.getUserContact(registrationForm.getMail());
        UserContact contactPhone = userContactService.getUserContact(registrationForm.getPhone());

        contactEmail.setUser(user);
        contactPhone.setUser(user);

        user.getUserContact().add(contactEmail);
        user.getUserContact().add(contactPhone);

        userRepository.save(user);
        userContactService.save(contactEmail);
        userContactService.save(contactPhone);

        bookShopService.addBooksType(userOld, user.getId());
        return user;
    }

    public ContactConfirmationResponse handlerRequestNewContactConfirmation(ContactConfirmationPayload payload) {
        UserContact userContact = userContactService.getUserContact(payload.getContact());
        if (userContact != null && userContact.getApproved() == (short) 1) {
            String messagePhone = messageLocale.getMessage("message.phoneBusy");
            String messageMail = messageLocale.getMessage("message.mailBusy");
            String error = userContact.getType().equals(ContactType.PHONE)
                    ? messagePhone
                    : messageMail;
            return new ContactConfirmationResponse(false, error);
        }
        if (userContact != null && userContact.getApproved() == (short) 0) {
            long dif = Math.abs(userContact.getCodeTime().getTime() - new Date().getTime());
            if (userContact.getCodeTrails() > 2 && dif < 300000) {
                return blockContact(dif);
            }
            userContactService.changeContact(userContact);
        } else {
            userContactService.createNewContact(payload);
        }
        return new ContactConfirmationResponse(true);
    }

    protected ContactConfirmationResponse blockContact(long time) {
        String message = messageLocale.getMessage("message.blockContactApproved");
        return new ContactConfirmationResponse(false,
                generator.generatorTextBlockContact(time, message));
    }
}