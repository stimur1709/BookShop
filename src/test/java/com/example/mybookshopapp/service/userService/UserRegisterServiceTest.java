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
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource("/application.yaml")
@Slf4j
class UserRegisterServiceTest {

    private final UserContactRepository userContactRepository;
    private final UserRegisterService userRegisterService;
    private final MessageSource messageSource;
    private final Generator generator;

    private static String mail;
    private static String phone;

    @Autowired
    UserRegisterServiceTest(UserContactRepository userContactRepository, UserRegisterService userRegisterService, MessageSource messageSource, Generator generator) {
        this.userContactRepository = userContactRepository;
        this.userRegisterService = userRegisterService;
        this.messageSource = messageSource;
        this.generator = generator;
    }

    @BeforeEach
    void setUp() {
        mail = "stimur1794@mail.ru";
        phone = "+7 (999) 111-11-22";
    }

    @AfterEach
    void tearDown() {
        userContactRepository.deleteByContact(mail);
        userContactRepository.deleteByContact(phone);
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

}