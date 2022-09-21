package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.security.BookstoreUserDetails;
import com.example.mybookshopapp.security.jwt.JWTUtil;
import com.example.mybookshopapp.security.model.ContactConfirmationPayload;
import com.example.mybookshopapp.security.model.ContactConfirmationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class UserAuthService {

    private final AuthenticationManager authenticationManager;
    private final BookStoreUserDetailsService bookStoreUserDetailsService;
    private final JWTUtil jwtUtil;
    private final Random random;
    private final UserContactService userContactService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserAuthService(AuthenticationManager authenticationManager, BookStoreUserDetailsService bookStoreUserDetailsService,
                           JWTUtil jwtUtil, Random random, UserContactService userContactService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.bookStoreUserDetailsService = bookStoreUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.random = random;
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
                return blockContact(true);

            userContact.setCodeTrails(userContact.getCodeTrails() + 1);
            userContactService.save(userContact);
            return badContact(userContact.getCodeTrails());
        }
    }

    public ContactConfirmationResponse handleRequestContactConfirmation(ContactConfirmationPayload payload) {
        UserContact userContact = userContactService.getUserContact(payload.getContact());
        if (userContact != null) {
            long dif = Math.abs(userContact.getCodeTime().getTime() - new Date().getTime());

            if (userContact.getCodeTrails() >= 2 && dif < 300000) {
                return blockContact(false);
            }

            String res = getSecretCode();
            System.out.println(res);
            userContact.setCodeTrails(0);
            userContact.setCodeTime(new Date());
            userContact.setCode(passwordEncoder.encode(res));
            userContactService.save(userContact);
            return new ContactConfirmationResponse(true);
        }
        return new ContactConfirmationResponse(true);
    }


    private String getSecretCode() {
        return 100 + random.nextInt(999 - 100 + 1) + " " + (100 + random.nextInt(999 - 100 + 1));
    }

    private ContactConfirmationResponse blockContact(boolean result) {
        ContactConfirmationResponse response = new ContactConfirmationResponse(result);
        response.setError("Количество попыток входа по телефону исчерпано, попробуйте войти по e-mail или повторить вход по телефону через 5 минут");
        return response;
    }

    private ContactConfirmationResponse badContact(int result) {
        int count = 3 - result;
        String text = count == 1 ? "Код подтверждения введён неверно. У вас осталось " + count + " попытка"
                : "Код подтверждения введён неверно. У вас осталось " + count + " попытки";
        return new ContactConfirmationResponse(false, text);
    }

}
