package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.RegistrationForm;
import com.example.mybookshopapp.model.enums.ContactType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.yaml")
class UserAuthControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private static String firstname;
    private static String lastname;
    private static String mail;
    private static String phone;
    private static String password;

    @Autowired
    UserAuthControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeAll
    static void setUp() {
        firstname = "Timur";
        lastname = "Safin";
        mail = "stimur1794@mail.ru";
        phone = "+7 (999) 111-11-22";
        password = "987654321";
    }

    @Test
    void registrationUSer() throws Exception {
        String mailContact = objectMapper.writeValueAsString(new ContactConfirmationPayload(mail, ContactType.MAIL));
        String phoneContact = objectMapper.writeValueAsString(new ContactConfirmationPayload(phone, ContactType.PHONE));
        System.out.println(mailContact);
        mockMvc.perform(
                post("/api/requestContactConfirmation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mailContact)
        ).andExpect(status().isOk());

        mockMvc.perform(
                post("/api/requestContactConfirmation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(phoneContact)
        ).andExpect(status().isOk());

        String regForm = objectMapper.writeValueAsString(new RegistrationForm(firstname, lastname, mail, phone, password));
        System.out.println(regForm);
        mockMvc.perform(
                post("/registration")
                        .param("firstname", firstname)
                        .param("lastname", lastname)
                        .param("mail", mail)
                        .param("phone", phone)
                        .param("password", password)
        ).andExpect(xpath("/html/body/div/div[2]/main/form/div/div[1]/div[1]/span/label").string("РЕГИСТРАЦИЯ ПРОШЛА УСПЕШНО!"));
    }
}