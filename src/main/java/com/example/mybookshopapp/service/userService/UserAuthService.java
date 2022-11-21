package com.example.mybookshopapp.service.userService;

import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.security.BookstoreUserDetails;
import com.example.mybookshopapp.security.token.JWTUtil;
import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.ContactConfirmationResponse;
import com.example.mybookshopapp.service.BlacklistService;
import com.example.mybookshopapp.service.BookStoreUserDetailsService;
import com.example.mybookshopapp.service.UserContactService;
import com.example.mybookshopapp.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class UserAuthService {

    private final JWTUtil jwtUtil;
    private final BlacklistService blacklistService;
    private final UserContactService userContactService;
    private final Generator generator;
    private final AuthenticationManager authenticationManager;
    private final BookStoreUserDetailsService bookStoreUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;
    private final HttpServletRequest request;

    @Autowired
    public UserAuthService(JWTUtil jwtUtil, BlacklistService blacklistService,
                           UserContactService userContactService, Generator generator,
                           AuthenticationManager authenticationManager, BookStoreUserDetailsService bookStoreUserDetailsService,
                           PasswordEncoder passwordEncoder, MessageSource messageSource, LocaleResolver localeResolver, HttpServletRequest request) {
        this.jwtUtil = jwtUtil;
        this.blacklistService = blacklistService;
        this.userContactService = userContactService;
        this.generator = generator;
        this.authenticationManager = authenticationManager;
        this.bookStoreUserDetailsService = bookStoreUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
        this.request = request;
    }


    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) {
        UserContact userContact = userContactService.getUserContact(payload.getContact());
        if (userContact == null) {
            String message = messageSource.getMessage("message.userNotFound", null, localeResolver.resolveLocale(request));
            return new ContactConfirmationResponse(false, message);
        }
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userContact.getUser().getHash(),
                    payload.getCode()));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            BookstoreUserDetails userDetails = (BookstoreUserDetails) bookStoreUserDetailsService.loadUserByUsername(userContact.getUser().getHash());
            String jwtToken = jwtUtil.generateToken(userDetails);
            blacklistService.delete(jwtToken);
            return new ContactConfirmationResponse(true, jwtToken);
        } catch (Exception e) {

            if (userContact.getCodeTrails() >= 2) {
                long dif = Math.abs(userContact.getCodeTime().getTime() - new Date().getTime());
                return blockContact(userContact.getType(), dif);
            }

            userContact.setCodeTrails(userContact.getCodeTrails() + 1);
            userContactService.save(userContact);
            return badContact(userContact.getCodeTrails());
        }
    }

    public ContactConfirmationResponse handlerApproveContact(ContactConfirmationPayload payload) {
        UserContact userContact = userContactService.getUserContact(payload.getContact());
        long dif = Math.abs(userContact.getCodeTime().getTime() - new Date().getTime());

        if (!passwordEncoder.matches(payload.getCode(), userContact.getCode())) {
            userContact.setCodeTrails(userContact.getCodeTrails() + 1);
            userContactService.save(userContact);
            if (userContact.getCodeTrails() > 2 && dif < 300000) {
                return blockContact(dif);
            }
            return badContact(userContact.getCodeTrails());
        }

        if (dif > 1000000) {
            String message = messageSource.getMessage("message.newCode", null, localeResolver.resolveLocale(request));
            return new ContactConfirmationResponse(false, message);
        }

        userContact.setApproved((short) 1);
        userContactService.save(userContact);

        ContactConfirmationResponse response = new ContactConfirmationResponse(true, userContact.getType());

        if (userContact.getParentUserContact() != null) {
            userContactService.delete(userContact.getParentUserContact());
        }

        return response;
    }

    public ContactConfirmationResponse handlerRequestContactConfirmation(ContactConfirmationPayload payload) {
        UserContact userContact = userContactService.getUserContact(payload.getContact());
        if (userContact != null) {
            long dif = Math.abs(userContact.getCodeTime().getTime() - new Date().getTime());

            if (userContact.getCodeTrails() >= 2 && dif < 300000) {
                return blockContact(payload.getContactType(), dif);
            }

            userContact.setCodeTrails(0);
            userContact.setCodeTime(new Date());
            userContactService.save(userContact);
            return new ContactConfirmationResponse(true);
        }
        return new ContactConfirmationResponse(true);
    }

    private ContactConfirmationResponse blockContact(ContactType type, long time) {
        ContactConfirmationResponse response = new ContactConfirmationResponse(false);
        String messagePhone = messageSource.getMessage("message.blockContactPhone", null, localeResolver.resolveLocale(request));
        String messageMail = messageSource.getMessage("message.blockContactMail", null, localeResolver.resolveLocale(request));
        response.setError(type.equals(ContactType.PHONE)
                ? generator.generatorTextBlockContact(time, messagePhone)
                : generator.generatorTextBlockContact(time, messageMail));
        return response;
    }

    private ContactConfirmationResponse blockContact(long time) {
        String message = messageSource.getMessage("message.blockContactApproved", null, localeResolver.resolveLocale(request));
        return new ContactConfirmationResponse(false,
                generator.generatorTextBlockContact(time, message));
    }

    private ContactConfirmationResponse badContact(int result) {
        ContactConfirmationResponse contactConfirmationResponse = new ContactConfirmationResponse();
        contactConfirmationResponse.setResult(true);
        contactConfirmationResponse.setError(generator.generatorTextBadContact(result));
        return contactConfirmationResponse;
    }

}
