package com.example.mybookshopapp.service.userService;

import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.RegistrationForm;
import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.repository.UserContactRepository;
import com.example.mybookshopapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource("/application.yaml")
@Slf4j
class UserRegisterServiceTest {

    private final UserRepository userRepository;
    private final UserContactRepository userContactRepository;
    private final UserRegisterService userRegisterService;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletResponse response;
    private final HttpServletRequest request;

    private static String firstname;
    private static String lastname;
    private static String mail;
    private static String phone;
    private static String password;
    private static RegistrationForm registrationForm;
    private String hash;

    @Autowired
    UserRegisterServiceTest(UserRepository userRepository, UserContactRepository userContactRepository, UserRegisterService userRegisterService, PasswordEncoder passwordEncoder, HttpServletResponse response, HttpServletRequest request) {
        this.userRepository = userRepository;
        this.userContactRepository = userContactRepository;
        this.userRegisterService = userRegisterService;
        this.passwordEncoder = passwordEncoder;
        this.response = response;
        this.request = request;
    }

    @BeforeEach
    void setUp() {
        response.addCookie(new Cookie("cartContent", "malenkiy-prints"));
        firstname = "Timur";
        lastname = "Safin";
        mail = "stimur1794@mail.ru";
        phone = "+7 (999) 111-11-22";
        password = "987654321";
        registrationForm = new RegistrationForm(firstname, lastname, mail, phone, passwordEncoder.encode(password));
    }

    @AfterEach
    void tearDown() {
        userContactRepository.deleteByContact(mail);
        userContactRepository.deleteByContact(phone);
        long l = userRepository.deleteByHash(hash);
        log.info("Пользователь " + hash + " удален " + l);
    }

    @Test
    void registerUser() {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName() + " !!!");
            }
        }
        userRegisterService.handlerRequestNewContactConfirmation(new ContactConfirmationPayload(mail, ContactType.MAIL));
        userRegisterService.handlerRequestNewContactConfirmation(new ContactConfirmationPayload(phone, ContactType.PHONE));

        User user = userRegisterService.registerUser(registrationForm);
        hash = user.getHash();
        log.info("Пользователь " + hash + " создан");

        assertNotNull(user);
        assertTrue(passwordEncoder.matches(registrationForm.getPassword(), user.getPassword()));
        assertTrue(CoreMatchers.is(user.getFirstname()).matches(registrationForm.getFirstname()));

    }

}