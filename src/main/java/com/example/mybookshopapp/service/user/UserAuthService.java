package com.example.mybookshopapp.service.user;

import com.example.mybookshopapp.data.entity.enums.ContactType;
import com.example.mybookshopapp.data.entity.user.UserContact;
import com.example.mybookshopapp.data.entity.user.UserRole;
import com.example.mybookshopapp.data.outher.ContactConfirmationPayload;
import com.example.mybookshopapp.data.outher.ContactConfirmationResponse;
import com.example.mybookshopapp.security.BookstoreUserDetails;
import com.example.mybookshopapp.security.token.JWTUtil;
import com.example.mybookshopapp.service.BlacklistService;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.BookStoreUserDetailsService;
import com.example.mybookshopapp.service.UserContactService;
import com.example.mybookshopapp.util.Generator;
import com.example.mybookshopapp.util.MessageLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAuthService {

    private final JWTUtil jwtUtil;
    private final BlacklistService blacklistService;
    private final UserContactService userContactService;
    private final Generator generator;
    private final AuthenticationManager authenticationManager;
    private final BookStoreUserDetailsService bookStoreUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletResponse response;
    private final BookShopService bookShopService;
    private final UserProfileService userProfileService;
    private final MessageLocale messageLocale;

    @Autowired
    public UserAuthService(JWTUtil jwtUtil, BlacklistService blacklistService,
                           UserContactService userContactService, Generator generator,
                           AuthenticationManager authenticationManager, BookStoreUserDetailsService bookStoreUserDetailsService,
                           PasswordEncoder passwordEncoder, HttpServletResponse response, BookShopService bookShopService, UserProfileService userProfileService, MessageLocale messageLocale) {
        this.jwtUtil = jwtUtil;
        this.blacklistService = blacklistService;
        this.userContactService = userContactService;
        this.generator = generator;
        this.authenticationManager = authenticationManager;
        this.bookStoreUserDetailsService = bookStoreUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.response = response;
        this.bookShopService = bookShopService;
        this.userProfileService = userProfileService;
        this.messageLocale = messageLocale;
    }

    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) {
        Integer userOld = userProfileService.getUserId();
        UserContact userContact = userContactService.getUserContact(payload.getContact());
        if (userContact == null) {
            String message = messageLocale.getMessage("message.userNotFound");
            return new ContactConfirmationResponse(false, message);
        }
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userContact.getUser().getHash(), payload.getCode()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            BookstoreUserDetails userDetails = (BookstoreUserDetails) bookStoreUserDetailsService.loadUserByUsername(userContact.getUser().getHash());
            String jwtToken = jwtUtil.generateToken(userDetails);
            blacklistService.delete(jwtToken);
            Cookie cookie = new Cookie("token", jwtToken);
            response.addCookie(cookie);
            bookShopService.addBooksType(userOld, userContact.getUser().getId());
            return new ContactConfirmationResponse(true, jwtToken, getUserRole(userContact));
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
            if (dif > 300000) {
                String message = messageLocale.getMessage("message.newCode");
                return new ContactConfirmationResponse(false, message);
            }
            return badContact(userContact.getCodeTrails());
        }

        if (dif > 1000000) {
            String message = messageLocale.getMessage("message.newCode");
            return new ContactConfirmationResponse(false, message);
        }

        userContact.setApproved((short) 1);
        userContactService.save(userContact);

        if (userContact.getParentUserContact() != null) {
            userContactService.delete(userContact.getParentUserContact());
        }

        return new ContactConfirmationResponse(true, userContact.getType());
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
        ContactConfirmationResponse contactConfirmationResponse = new ContactConfirmationResponse(false);
        String messagePhone = messageLocale.getMessage("message.blockContactPhone");
        String messageMail = messageLocale.getMessage("message.blockContactMail");
        contactConfirmationResponse.setError(type.equals(ContactType.PHONE)
                ? generator.generatorTextBlockContact(time, messagePhone)
                : generator.generatorTextBlockContact(time, messageMail));
        return contactConfirmationResponse;
    }

    private ContactConfirmationResponse blockContact(long time) {
        String message = messageLocale.getMessage("message.blockContactApproved");
        return new ContactConfirmationResponse(false,
                generator.generatorTextBlockContact(time, message));
    }

    private ContactConfirmationResponse badContact(int result) {
        ContactConfirmationResponse contactConfirmationResponse = new ContactConfirmationResponse();
        contactConfirmationResponse.setResult(true);
        contactConfirmationResponse.setError(generator.generatorTextBadContact(result));
        return contactConfirmationResponse;
    }

    private List<String> getUserRole(UserContact userContact) {
        return userContact.getUser()
                .getUserRoles()
                .stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList());
    }

}
