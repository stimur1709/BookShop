package com.example.mybookshopapp.service.userService;

import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.ContactConfirmationResponse;
import com.example.mybookshopapp.dto.RegistrationForm;
import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserContactRepository;
import com.example.mybookshopapp.repository.UserRepository;
import com.example.mybookshopapp.util.Generator;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application.yaml")
@Slf4j
class UserRegisterServiceTest {

    private final UserRepository userRepository;
    private final UserContactRepository userContactRepository;
    private final UserRegisterService userRegisterService;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final Generator generator;

    private static String firstname;
    private static String lastname;
    private static String mail;
    private static String phone;
    private static String password;
    private static RegistrationForm registrationForm;
    private String hash;

    @Autowired
    UserRegisterServiceTest(UserRepository userRepository, UserContactRepository userContactRepository, UserRegisterService userRegisterService, PasswordEncoder passwordEncoder, MessageSource messageSource, Generator generator) {
        this.userRepository = userRepository;
        this.userContactRepository = userContactRepository;
        this.userRegisterService = userRegisterService;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = messageSource;
        this.generator = generator;
    }

    @BeforeEach
    void setUp() {
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
    @DisplayName("Создание существующего контакта")
    void creatingAnExistingUserContact() {
        UserContact contact = userContactRepository.findFirstByApproved((short) 1);
        ContactConfirmationResponse response = userRegisterService.handlerRequestNewContactConfirmation(new ContactConfirmationPayload(contact.getContact(), contact.getType()));
        String message = contact.getType().equals(ContactType.PHONE)
                ? messageSource.getMessage("message.phoneBusy", null, new Locale("ru"))
                : messageSource.getMessage("message.mailBusy", null, new Locale("ru"));
        assertEquals(response.getError(), message);
    }

    @Test
    @DisplayName("Создание заблокированного контакта")
    void testBlockingUserContact() throws InterruptedException {
        UserContact contact = new UserContact(ContactType.MAIL, mail);
        contact.setCodeTrails(3);
        contact.setApproved((short) 0);
        contact = userContactRepository.save(contact);
        String message = messageSource.getMessage("message.blockContactApproved", null, new Locale("ru"));

        for (int i = 0; i < 6; i++) {
            ContactConfirmationResponse response = userRegisterService.handlerRequestNewContactConfirmation(new ContactConfirmationPayload(contact.getContact(), contact.getType()));
            long dif = Math.abs(contact.getCodeTime().getTime() - new Date().getTime());
            if (i != 5) {
                log.info("Попытка создать заблокированный контакт: {}", response.getError());
                assertEquals(response.getError(), generator.generatorTextBlockContact(dif, message));
                Thread.sleep(60000);
            } else {
                log.info("Попытка создать заблокированный контакт спустя 5 минут после блокировки");
                assertTrue(response.isResult());
            }
        }
    }

    @Test
    void registerUser() {
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