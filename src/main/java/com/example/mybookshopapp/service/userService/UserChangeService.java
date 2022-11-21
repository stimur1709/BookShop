package com.example.mybookshopapp.service.userService;

import com.example.mybookshopapp.dto.ChangeProfileForm;
import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.ContactConfirmationResponse;
import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserRepository;
import com.example.mybookshopapp.service.UserContactService;
import com.example.mybookshopapp.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserChangeService {

    private final UserProfileService userProfileService;
    private final UserContactService userContactService;
    private final Generator generator;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;
    private final HttpServletRequest request;

    @Autowired
    public UserChangeService(UserProfileService userProfileService, UserContactService userContactService,
                             Generator generator, PasswordEncoder passwordEncoder, UserRepository userRepository,
                             MessageSource messageSource, LocaleResolver localeResolver, HttpServletRequest request) {
        this.userProfileService = userProfileService;
        this.userContactService = userContactService;
        this.generator = generator;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
        this.request = request;
    }

    public ContactConfirmationResponse handlerRequestChangeContactConfirmation(ContactConfirmationPayload payload) {
        Optional<UserContact> userContact = userContactService.checkUserExistsByContact(payload.getContact());
        if (userContact.isPresent()) {
            if (userContact.get().getApproved() == (short) 1) {
                String messagePhone = messageSource.getMessage("message.phoneBusy", null, localeResolver.resolveLocale(request));
                String messageMail = messageSource.getMessage("message.mailBusy", null, localeResolver.resolveLocale(request));
                String error = payload.getContactType().equals(ContactType.PHONE)
                        ? messagePhone
                        : messageMail;
                return new ContactConfirmationResponse(false, error);
            }

            long dif = Math.abs(userContact.get().getCodeTime().getTime() - new Date().getTime());
            if (dif <= 300000 && userContact.get().getCodeTrails() >= 2) {
                String message = messageSource.getMessage("message.blockContactApproved", null, localeResolver.resolveLocale(request));
                return new ContactConfirmationResponse(false,
                        generator.generatorTextBlockContact(dif, message));
            } else {
                userContactService.changeContact(userContact.get());
                return new ContactConfirmationResponse(true);
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
                    payload.getContactType(), payload.getContact());
            userContactService.save(userNewContact);
        }
        return new ContactConfirmationResponse(true);
    }

    public Map<String, Object> updateUser(ChangeProfileForm changeProfileForm) {
        Map<String, Object> response = new HashMap<>();
        User user = userProfileService.getCurrentUser();
        user.setFirstname(changeProfileForm.getFirstname());
        user.setLastname(changeProfileForm.getLastname());
        user.setPassword(passwordEncoder.encode(changeProfileForm.getPassword()));

        if (!changeProfileForm.getEmail().equals(changeProfileForm.getOldEmail())) {
            if (userContactService.checkUserExistsByContact(changeProfileForm.getEmail()).isPresent()) {
                response.put("email", messageSource.getMessage("message.mailBusy", null, localeResolver.resolveLocale(request)));
            } else {
                UserContact userContact = userContactService.getUserContact(changeProfileForm.getOldEmail());
                user.getUserContact().add(userContactService.changeContact(new UserContact(ContactType.MAIL, changeProfileForm.getEmail(), userContact), user));
            }
        }
        if (!changeProfileForm.getPhone().equals(changeProfileForm.getOldPhone())) {
            if (userContactService.checkUserExistsByContact(changeProfileForm.getPhone()).isPresent()) {
                response.put("phone", messageSource.getMessage("message.phoneBusy", null, localeResolver.resolveLocale(request)));
            } else {
                UserContact userContact = userContactService.getUserContact(changeProfileForm.getOldPhone());
                user.getUserContact().add(userContactService.changeContact(new UserContact(ContactType.PHONE, changeProfileForm.getPhone(), userContact), user));
            }
        }
        userRepository.save(user);
        return response;
    }

}

