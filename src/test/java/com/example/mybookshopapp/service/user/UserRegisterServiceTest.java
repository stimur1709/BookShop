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
import org.springframework.test.context.TestPropertySource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@Slf4j
@DisplayName("Создание контакта")
class UserRegisterServiceTest {

    private final UserContactRepository userContactRepository;
    private final UserRegisterService userRegisterService;
    private final MessageLocale messageLocale;
    private final Generator generator;

    private static String mail;
    private static String phone;

    @Autowired
    UserRegisterServiceTest(UserContactRepository userContactRepository, UserRegisterService userRegisterService,
                            MessageLocale messageLocale, Generator generator) {
        this.userContactRepository = userContactRepository;
        this.userRegisterService = userRegisterService;
        this.messageLocale = messageLocale;
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
                ? messageLocale.getMessage("message.phoneBusy")
                : messageLocale.getMessage("message.mailBusy");
        assertEquals(response.getError(), message);
    }

    @Test
    @DisplayName("Создание заблокированного контакта")
    void testBlockingUserContact() {
        UserContact contact = new UserContact(ContactType.MAIL, mail);
        contact.setCodeTrails(3);
        contact.setApproved((short) 0);
        contact = userContactRepository.save(contact);
        String message = messageLocale.getMessage("message.blockContactApproved");

        ContactConfirmationPayload payload = new ContactConfirmationPayload(contact.getContact(), contact.getType());
        ContactConfirmationResponse response = userRegisterService.handlerRequestNewContactConfirmation(payload);
        long dif = Math.abs(contact.getCodeTime().getTime() - new Date().getTime());
        log.info("Попытка создать заблокированный контакт: {}", response.getError());
        assertEquals(response.getError(), generator.generatorTextBlockContact(dif, message));
    }

}