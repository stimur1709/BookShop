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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Autowired
    public UserAuthService(JWTUtil jwtUtil, BlacklistService blacklistService,
                           UserContactService userContactService, Generator generator,
                           AuthenticationManager authenticationManager, BookStoreUserDetailsService bookStoreUserDetailsService,
                           PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.blacklistService = blacklistService;
        this.userContactService = userContactService;
        this.generator = generator;
        this.authenticationManager = authenticationManager;
        this.bookStoreUserDetailsService = bookStoreUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) {
        UserContact userContact = userContactService.getUserContact(payload.getContact());
        if (userContact == null)
            return new ContactConfirmationResponse(false, "Пользователь не найден");

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
                return blockContact(true, userContact.getType(), dif);
            }

            userContact.setCodeTrails(userContact.getCodeTrails() + 1);
            userContactService.save(userContact);
            return badContact(userContact.getCodeTrails(), userContact.getType());
        }
    }

    public ContactConfirmationResponse handlerApproveContact(ContactConfirmationPayload payload) {
        UserContact userContact = userContactService.getUserContact(payload.getContact());
        long dif = Math.abs(userContact.getCodeTime().getTime() - new Date().getTime());

        if (userContact.getCodeTrails() > 2 && dif < 300000) {
            return blockContact(dif);
        }

        if (dif > 1000000) {
            return new ContactConfirmationResponse(false, "Код подтверждения устарел. Запросите новый");
        }

        if (!passwordEncoder.matches(payload.getCode(), userContact.getCode())) {
            userContact.setCodeTrails(userContact.getCodeTrails() + 1);
            userContactService.save(userContact);
            return badContact(userContact.getCodeTrails(), userContact.getType());
        }

        userContact.setApproved((short) 1);
        userContactService.save(userContact);

        ContactConfirmationResponse response = new ContactConfirmationResponse(true);

        if (userContact.getParentUserContact() != null) {
            userContactService.delete(userContact.getParentUserContact());
        }

        return response;
    }

    public ContactConfirmationResponse handlerRequestContactConfirmation(ContactConfirmationPayload payload) {
        UserContact userContact = userContactService.getUserContact(payload.getContact());
        if (userContact != null) {
            long dif = Math.abs(userContact.getCodeTime().getTime() - new Date().getTime());

            if (userContact.getCodeTrails() > 2 && dif < 300000) {
                return blockContact(false, payload.getContactType(), dif);
            }

            userContact.setCodeTrails(0);
            userContact.setCodeTime(new Date());
            userContact.setCode(passwordEncoder.encode(generator.getSecretCode()));
            userContactService.save(userContact);
            return new ContactConfirmationResponse(true);
        }
        return new ContactConfirmationResponse(true);
    }

    private ContactConfirmationResponse blockContact(boolean result, ContactType type, long time) {
        ContactConfirmationResponse response = new ContactConfirmationResponse(result);
        response.setError(type.equals(ContactType.PHONE)
                ? generator.generatorTextBlockContact(time, "Количество попыток входа по телефону исчерпано, попробуйте войти по e-mail или повторить вход по телефону через ")
                : generator.generatorTextBlockContact(time, "Количество попыток входа по e-mail исчерпано, попробуйте войти по телефону или повторить вход по e-mail через "));
        return response;
    }

    private ContactConfirmationResponse blockContact(long time) {
        return new ContactConfirmationResponse(false,
                generator.generatorTextBlockContact(time, "Число попыток подтверждения превышено, повторите попытку через "));
    }

    private ContactConfirmationResponse badContact(int result, ContactType type) {
        ContactConfirmationResponse contactConfirmationResponse = new ContactConfirmationResponse();
        contactConfirmationResponse.setResult(true);
        contactConfirmationResponse.setError(generator.generatorTextBadContact(type, result));
        return contactConfirmationResponse;
    }


    private BookstoreUserDetails auth(ContactConfirmationPayload payload) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return (BookstoreUserDetails) bookStoreUserDetailsService.loadUserByUsername(payload.getContact());
    }
}
