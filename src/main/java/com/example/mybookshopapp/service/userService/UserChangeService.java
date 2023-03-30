package com.example.mybookshopapp.service.userService;

import com.example.mybookshopapp.data.entity.enums.ContactType;
import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.data.entity.user.UserContact;
import com.example.mybookshopapp.data.outher.ChangeProfileForm;
import com.example.mybookshopapp.data.outher.ContactConfirmationPayload;
import com.example.mybookshopapp.data.outher.ContactConfirmationResponse;
import com.example.mybookshopapp.data.outher.RestorePassword;
import com.example.mybookshopapp.repository.UserRepository;
import com.example.mybookshopapp.service.UserContactService;
import com.example.mybookshopapp.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        if (payload.getOldContact() != null) {
            UserContact userOldContact = userContactService.getUserContact(payload.getOldContact());
            userContactService.createNewContact(userOldContact, payload);
        } else {
            userContactService.createNewContact(userProfileService.getCurrentUser(), payload);
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

    @Async
    @Transactional
    public void restorePassword(String contact) {
        Optional<UserContact> userContact = userContactService.checkUserExistsByContact(contact);
        if (userContact.isPresent()) {
            String code = userContactService.getConfirmationCode(contact, userContact.get().getType(), 4);
            userContact.get().setCode(code);
            userContactService.save(userContact.get());
        }
    }

    @Async
    @Transactional
    public void changePassword(String contact, RestorePassword restorePassword) {
        UserContact userContact = userContactService.getUserContact(contact);
        if (userContact != null && passwordEncoder.matches(restorePassword.getCode(), userContact.getCode())) {
            User user = userContact.getUser();
            user.setPassword(passwordEncoder.encode(restorePassword.getPassword()));
            userRepository.save(user);
        }
    }
}

