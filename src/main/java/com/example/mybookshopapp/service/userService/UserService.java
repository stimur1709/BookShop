package com.example.mybookshopapp.service.userService;

import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.ContactConfirmationResponse;
import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserRepository;
import com.example.mybookshopapp.security.BookstoreUserDetails;
import com.example.mybookshopapp.security.token.JWTUtil;
import com.example.mybookshopapp.service.Book2UserTypeService;
import com.example.mybookshopapp.service.BookStoreUserDetailsService;
import com.example.mybookshopapp.service.UserContactService;
import com.example.mybookshopapp.util.Generator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    protected final UserRepository userRepository;
    protected final PasswordEncoder passwordEncoder;
    protected final Book2UserTypeService book2UserTypeService;
    protected final UserContactService userContactService;
    protected final Generator generator;
    protected final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final BookStoreUserDetailsService bookStoreUserDetailsService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       Book2UserTypeService book2UserTypeService, UserContactService userContactService,
                       Generator generator, JWTUtil jwtUtil, AuthenticationManager authenticationManager, BookStoreUserDetailsService bookStoreUserDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.book2UserTypeService = book2UserTypeService;
        this.userContactService = userContactService;
        this.generator = generator;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.bookStoreUserDetailsService = bookStoreUserDetailsService;
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
            auth(payload);
            response.setToken(jwtUtil.generateToken(userContact.getContact()));
            userContactService.delete(userContact.getParentUserContact());
        }

        return response;
    }

    protected ContactConfirmationResponse blockContact(long time) {
        return new ContactConfirmationResponse(false,
                generator.generatorTextBlockContact(time, "Число попыток подтверждения превышено, повторите попытку через "));
    }

    protected ContactConfirmationResponse badContact(int result, ContactType type) {
        ContactConfirmationResponse response = new ContactConfirmationResponse(true);
        response.setError(generator.generatorTextBadContact(type, result));
        return response;
    }

    public BookstoreUserDetails auth(ContactConfirmationPayload payload) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return (BookstoreUserDetails) bookStoreUserDetailsService.loadUserByUsername(payload.getContact());
    }

}
