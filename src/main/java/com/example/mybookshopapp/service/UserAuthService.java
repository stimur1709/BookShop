package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.security.BookstoreUserDetails;
import com.example.mybookshopapp.security.jwt.JWTUtil;
import com.example.mybookshopapp.security.model.ContactConfirmationPayload;
import com.example.mybookshopapp.security.model.ContactConfirmationResponse;
import com.example.mybookshopapp.util.SecretCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserAuthService {

    private final AuthenticationManager authenticationManager;
    private final BookStoreUserDetailsService bookStoreUserDetailsService;
    private final JWTUtil jwtUtil;
    private final SecretCode secretCode;
    private final UserContactService userContactService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserAuthService(AuthenticationManager authenticationManager, BookStoreUserDetailsService bookStoreUserDetailsService,
                           JWTUtil jwtUtil, SecretCode secretCode, UserContactService userContactService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.bookStoreUserDetailsService = bookStoreUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.secretCode = secretCode;
        this.userContactService = userContactService;
        this.passwordEncoder = passwordEncoder;
    }

    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) {
        UserContact userContact = userContactService.getUserContact(payload.getContact());
        if (userContact == null)
            return new ContactConfirmationResponse(false, "Пользователь не найден");

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                    payload.getCode()));
            BookstoreUserDetails userDetails =
                    (BookstoreUserDetails) bookStoreUserDetailsService.loadUserByUsername(payload.getContact());
            String jwtToken = jwtUtil.generateToken(userDetails);
            return new ContactConfirmationResponse(true, jwtToken);
        } catch (Exception e) {

            if (userContact.getCodeTrails() >= 2)
                return blockContact(true, userContact.getType());

            userContact.setCodeTrails(userContact.getCodeTrails() + 1);
            userContactService.save(userContact);
            return badContact(userContact.getCodeTrails(), userContact.getType());
        }
    }

    public ContactConfirmationResponse handlerRequestContactConfirmation(ContactConfirmationPayload payload) {
        UserContact userContact = userContactService.getUserContact(payload.getContact());
        if (userContact != null) {
            long dif = Math.abs(userContact.getCodeTime().getTime() - new Date().getTime());

            if (userContact.getCodeTrails() >= 2 && dif < 300000) {
                return blockContact(false, payload.getContactType());
            }

            String res = secretCode.getSecretCode();
            userContact.setCodeTrails(0);
            userContact.setCodeTime(new Date());
            userContact.setCode(passwordEncoder.encode(res));
            userContactService.save(userContact);
            return new ContactConfirmationResponse(true);
        }
        return new ContactConfirmationResponse(true);
    }

    private ContactConfirmationResponse blockContact(boolean result, ContactType type) {
        ContactConfirmationResponse response = new ContactConfirmationResponse(result);
        response.setError(type.equals(ContactType.PHONE)
                ? "Количество попыток входа по телефону исчерпано, попробуйте войти по e-mail или повторить вход по телефону через 5 минут"
                : "Количество попыток входа по e-mail исчерпано, попробуйте войти по телефону или повторить вход по e-mail через 5 минут");
        return response;
    }

    private ContactConfirmationResponse badContact(int result, ContactType type) {
        int count = 3 - result;
        String pass = type.equals(ContactType.PHONE) ? "Код подтверждения" : "Пароль";
        String text = count == 1 ? pass + " введён неверно. У вас осталось " + count + " попытка"
                : pass + " введён неверно. У вас осталось " + count + " попытки";
        return new ContactConfirmationResponse(false, text);
    }

}