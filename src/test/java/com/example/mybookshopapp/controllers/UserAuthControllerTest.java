package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserContactRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.yaml")
@Slf4j
class UserAuthControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserContactRepository userContactRepository;

    private static String firstname;
    private static String lastname;
    private static String mail;
    private static String phone;
    private static String password;

    @Autowired
    UserAuthControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, UserContactRepository userContactRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userContactRepository = userContactRepository;
    }

    @BeforeEach
    void setUp() {
        firstname = "Timur";
        lastname = "Safin";
        mail = "stimur1794@mail.ru";
        phone = "+7 (999) 111-11-22";
        password = "987654321";
        UserContact contactMail = userContactRepository.save(new UserContact(ContactType.MAIL, mail));
        UserContact contactPhone = userContactRepository.save(new UserContact(ContactType.PHONE, phone));
        log.info("Контакты созданы: {}, {}", contactMail.getContact(), contactPhone.getContact());
    }

    @AfterEach
    void tearDown() {
        long l = userContactRepository.deleteByContact(mail);
        l += userContactRepository.deleteByContact(phone);
        log.info("Контакты удалены {}", l);
    }

    @Test
    void registrationUser() throws Exception {
        mockMvc.perform(
                post("/registration")
                        .param("firstname", firstname)
                        .param("lastname", lastname)
                        .param("mail", mail)
                        .param("phone", phone)
                        .param("password", password)
        ).andExpect(xpath("/html/head/title")
                .string("Книжный Магазин"));
    }
}