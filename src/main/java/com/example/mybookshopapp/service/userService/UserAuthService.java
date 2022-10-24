package com.example.mybookshopapp.service.userService;

import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserRepository;
import com.example.mybookshopapp.security.BookstoreUserDetails;
import com.example.mybookshopapp.security.token.JWTUtil;
import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.ContactConfirmationResponse;
import com.example.mybookshopapp.service.BlacklistService;
import com.example.mybookshopapp.service.Book2UserTypeService;
import com.example.mybookshopapp.service.BookStoreUserDetailsService;
import com.example.mybookshopapp.service.UserContactService;
import com.example.mybookshopapp.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserAuthService extends UserService {

    private final AuthenticationManager authenticationManager;
    private final BookStoreUserDetailsService bookStoreUserDetailsService;
    private final JWTUtil jwtUtil;
    private final BlacklistService blacklistService;

    @Autowired
    public UserAuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           Book2UserTypeService book2UserTypeService, UserContactService userContactService,
                           Generator generator, AuthenticationManager authenticationManager,
                           BookStoreUserDetailsService bookStoreUserDetailsService, JWTUtil jwtUtil,
                           BlacklistService blacklistService) {
        super(userRepository, passwordEncoder, book2UserTypeService, userContactService, generator);
        this.authenticationManager = authenticationManager;
        this.bookStoreUserDetailsService = bookStoreUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.blacklistService = blacklistService;
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

    public ContactConfirmationResponse handlerRequestContactConfirmation(ContactConfirmationPayload payload) {
        UserContact userContact = userContactService.getUserContact(payload.getContact());
        if (userContact != null) {
            long dif = Math.abs(userContact.getCodeTime().getTime() - new Date().getTime());

            if (userContact.getCodeTrails() > 2 && dif < 300000) {
                return blockContact(false, payload.getContactType(), dif);
            }

            String res = generator.getSecretCode();
            userContact.setCodeTrails(0);
            userContact.setCodeTime(new Date());
            userContact.setCode(passwordEncoder.encode(res));
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

    protected ContactConfirmationResponse badContact(int result, ContactType type) {
        return new ContactConfirmationResponse(false, generator.generatorTextBadContact(type, result));
    }
}
