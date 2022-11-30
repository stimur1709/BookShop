package com.example.mybookshopapp.service.userService;

import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.ContactConfirmationResponse;
import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserContactRepository;
import com.example.mybookshopapp.util.Generator;
import lombok.extern.slf4j.Slf4j;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@TestPropertySource("/application.yaml")
@Slf4j
class UserAuthServiceTest {

    private final UserAuthService userAuthService;
    private final UserContactRepository userContactRepository;
    private final PasswordEncoder passwordEncoder;
    private final Generator generator;
    private final MessageSource messageSource;

    private UserContact contactMail;
    private UserContact contactPhone;
    private String code;
    private String mail;
    private String phone;

    @Autowired
    UserAuthServiceTest(UserAuthService userAuthService, UserContactRepository userContactRepository,
                        PasswordEncoder passwordEncoder, Generator generator, MessageSource messageSource) {
        this.userAuthService = userAuthService;
        this.userContactRepository = userContactRepository;
        this.passwordEncoder = passwordEncoder;
        this.generator = generator;
        this.messageSource = messageSource;
    }

    @BeforeEach
    void setUp() {
        mail = "stimur1794@mail.ru";
        code = "123 456";
        phone = "+7 (999) 111-11-22";

        contactMail = userContactRepository.save(new UserContact(ContactType.MAIL, mail, passwordEncoder.encode(code)));
        contactPhone = userContactRepository.save(new UserContact(ContactType.PHONE, phone, passwordEncoder.encode(code.replace("123", "987"))));
    }

    @AfterEach
    void tearDown() {
        userContactRepository.deleteByContact(mail);
        userContactRepository.deleteByContact(phone);
    }

    @Test
    void jwtLogin() {
    }

    @Test
    @DisplayName("Подтверждение контакта")
    void handlerApproveContact() throws InterruptedException {
        ContactConfirmationPayload payload = new ContactConfirmationPayload(contactMail.getContact(), code);
        ContactConfirmationResponse response = userAuthService.handlerApproveContact(payload);

        assertTrue(response.isResult(), "Ошибка подтверждения");
        log.info("Контакт подтвержден");

        contactMail.setCodeTime(new Date(new Date().getTime() - 600000));
        userContactRepository.save(contactMail);
        response = userAuthService.handlerApproveContact(payload);
        String message = messageSource.getMessage("message.newCode", null, new Locale("ru"));
        assertEquals(response.getError(), message);
        log.info("Попытка подтверждения: {}", response.getError());


        for (int i = 1; i < 5; i++) {
            payload = new ContactConfirmationPayload(contactPhone.getContact(), code);
            response = userAuthService.handlerApproveContact(payload);
            if (i < 3) {
                assertEquals(response.getError(), generator.generatorTextBadContact(i));
                Thread.sleep(1000);
            } else {
                long dif = Math.abs(contactPhone.getCodeTime().getTime() - new Date().getTime());
                message = messageSource.getMessage("message.blockContactApproved", null, new Locale("ru"));
                assertEquals(response.getError(), generator.generatorTextBlockContact(dif, message));
            }
            log.info("Попытка подтверждения: {}", response.getError());
        }
    }

    @Test
    void handlerRequestContactConfirmation() {
    }
}