package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.enums.ContactType;
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
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode()));
        BookstoreUserDetails userDetails =
                (BookstoreUserDetails) bookStoreUserDetailsService.loadUserByUsername(payload.getContact());
        String jwtToken = jwtUtil.generateToken(userDetails);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }

    public ContactConfirmationResponse handleRequestContactConfirmation(ContactConfirmationPayload payload) {
        UserContact userContact = userContactService.getUserContact(payload.getContact());
        if (userContact != null && payload.getContactType().equals(ContactType.PHONE)) {
            String res = getSecretCode();
            System.out.println(res);
            userContact.setCode(passwordEncoder.encode(res));
            userContactService.save(userContact);
            return new ContactConfirmationResponse("true");
        }
        return new ContactConfirmationResponse("false");
    }



    private String getSecretCode() {
        return 100 + random.nextInt(999 - 100 + 1) + " " + (100 + random.nextInt(999 - 100 + 1));
    }

}
