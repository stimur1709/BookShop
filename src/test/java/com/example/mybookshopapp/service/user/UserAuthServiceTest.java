package com.example.mybookshopapp.service.user;

import com.example.mybookshopapp.data.entity.enums.ContactType;
import com.example.mybookshopapp.data.entity.user.UserContact;
import com.example.mybookshopapp.data.outher.ContactConfirmationPayload;
import com.example.mybookshopapp.data.outher.ContactConfirmationResponse;
import com.example.mybookshopapp.repository.UserContactRepository;
import com.example.mybookshopapp.util.Generator;
import com.example.mybookshopapp.util.MessageLocale;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@TestPropertySource("/application-test.properties")
@Slf4j
@DisplayName("Подтверждение контакта")
class UserAuthServiceTest {

    private final UserAuthService userAuthService;
    private final UserContactRepository userContactRepository;
    private final PasswordEncoder passwordEncoder;
    private final Generator generator;
    private final MessageLocale messageLocale;

    private UserContact contactMail;
    private UserContact contactPhone;
    private String code;
    private String mail;
    private String phone;

    @Autowired
    UserAuthServiceTest(UserAuthService userAuthService, UserContactRepository userContactRepository,
                        PasswordEncoder passwordEncoder, Generator generator, MessageLocale messageLocale) {
        this.userAuthService = userAuthService;
        this.userContactRepository = userContactRepository;
        this.passwordEncoder = passwordEncoder;
        this.generator = generator;
        this.messageLocale = messageLocale;
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
    @Transactional
    @DisplayName("Подтверждение контакта")
    void handlerApproveContact() throws InterruptedException {
        ContactConfirmationPayload payload = new ContactConfirmationPayload();
        payload.setContact(contactMail.getContact());
        payload.setCode(code);
        ContactConfirmationResponse response = userAuthService.handlerApproveContact(payload);

        assertTrue(response.isResult(), "Ошибка подтверждения");
        log.info("Контакт подтвержден");

        contactMail.setCodeTime(new Date(new Date().getTime() - 1000000));
        contactMail = userContactRepository.saveAndFlush(contactMail);
        response = userAuthService.handlerApproveContact(payload);
        String message = messageLocale.getMessage("message.newCode");
        assertEquals(response.getError(), message);
        log.info("Попытка подтверждения: {}", response.getError());
        for (int i = 1; i < 5; i++) {
            payload.setContact(contactPhone.getContact());
            payload.setCode(code);
            response = userAuthService.handlerApproveContact(payload);
            if (i < 3) {
                assertEquals(response.getError(), generator.generatorTextBadContact(i));
                Thread.sleep(1000);
            } else {
                long dif = Math.abs(contactPhone.getCodeTime().getTime() - new Date().getTime());
                message = messageLocale.getMessage("message.blockContactApproved");
                assertEquals(response.getError(), generator.generatorTextBlockContact(dif, message));
            }
            log.info("Попытка подтверждения: {}", response.getError());
        }
    }

    @Test
    @DisplayName("Проверка контакта перед логином")
    void handlerRequestContactConfirmation() {
        ContactConfirmationPayload payload = new ContactConfirmationPayload(contactMail.getContact(), contactMail.getType());
        ContactConfirmationResponse response = userAuthService.handlerRequestContactConfirmation(payload);
        assertTrue(response.isResult());

        contactMail.setCodeTrails(3);
        contactMail.setCodeTime(new Date());
        userContactRepository.save(contactMail);
        response = userAuthService.handlerRequestContactConfirmation(payload);
        String messageMail = messageLocale.getMessage("message.blockContactMail");
        long time = Math.abs(contactMail.getCodeTime().getTime() - new Date().getTime());
        assertEquals(response.getError(), generator.generatorTextBlockContact(time, messageMail));
    }

}