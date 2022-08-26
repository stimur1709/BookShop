package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserContactRepository;
import com.example.mybookshopapp.repository.UserRepository;
import com.example.mybookshopapp.security.BookstoreUserDetails;
import com.example.mybookshopapp.security.ContactConfirmationPayload;
import com.example.mybookshopapp.security.ContactConfirmationResponse;
import com.example.mybookshopapp.security.RegistrationForm;
import com.example.mybookshopapp.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegisterService {

    private final UserRepository userRepository;
    private final UserContactRepository userContactRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BookStoreUserDetailsService bookStoreUserDetailsService;
    private final JWTUtil jwtUtil;

    @Autowired
    public UserRegisterService(UserRepository userRepository,
                               UserContactRepository userContactRepository,
                               PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                               BookStoreUserDetailsService bookStoreUserDetailsService, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userContactRepository = userContactRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.bookStoreUserDetailsService = bookStoreUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public void registerUser(RegistrationForm registrationForm) {

        if (!userContactRepository.findUserContactEntityByContact(registrationForm.getEmail()).isPresent()) {
            User user = new User(registrationForm.getName(),
                    passwordEncoder.encode(registrationForm.getPassword()));
            UserContact contactEmail = new UserContact(user, ContactType.EMAIL, registrationForm.getEmail());
            UserContact contactPhone = new UserContact(user, ContactType.PHONE, registrationForm.getPhone());

            user.getUserContact().add(contactEmail);
            user.getUserContact().add(contactPhone);

            userRepository.save(user);
            userContactRepository.save(contactEmail);
            userContactRepository.save(contactPhone);
        }
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                payload.getContact(), payload.getCode()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
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
}
