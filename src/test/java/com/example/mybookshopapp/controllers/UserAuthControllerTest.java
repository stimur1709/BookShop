package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.data.entity.enums.ContactType;
import com.example.mybookshopapp.data.entity.user.UserContact;
import com.example.mybookshopapp.repository.UserContactRepository;
import com.example.mybookshopapp.util.GeneratorCookie;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.yaml")
@Slf4j
@DisplayName("Аутентификация пользователя")
class UserAuthControllerTest {

    private final MockMvc mockMvc;
    private final UserContactRepository userContactRepository;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;
    private final GeneratorCookie generatorCookie;

    private static String firstname;
    private static String lastname;
    private static String mail;
    private static String phone;
    private static String password;

    @Autowired
    UserAuthControllerTest(MockMvc mockMvc, UserContactRepository userContactRepository,
                           MessageSource messageSource, ObjectMapper objectMapper, GeneratorCookie generatorCookie) {
        this.mockMvc = mockMvc;
        this.userContactRepository = userContactRepository;
        this.messageSource = messageSource;
        this.objectMapper = objectMapper;
        this.generatorCookie = generatorCookie;
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
    @DisplayName("Проверка регистрации пользователя и перенос книг пользователя с cookies в БД")
    void registrationUser() throws Exception {
        mockMvc.perform(
                        post("/registration?lang=en")
                                .cookie(generatorCookie.createCookies(11))
                                .param("firstname", firstname)
                                .param("lastname", lastname)
                                .param("mail", mail)
                                .param("phone", phone)
                                .param("password", password)
                ).andExpect(xpath("/html/body/div/div[2]/main/form/div/div[1]/div[1]/span/label")
                        .string(messageSource.getMessage("message.registrationSuccessfully", null, Locale.ENGLISH)))
                .andExpect(xpath("//*[@id=\"cartt\"]")
                        .string("6"))
                .andExpect(xpath("//*[@id=\"keptt\"]")
                        .string("5"));
    }

    @Test
    @WithUserDetails("stimurstimurstimurs")
    @DisplayName("Проверка аутентификации пользователя и доступ к странице профиля")
    void authUser() throws Exception {
        mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div[2]/main/h2/text()")
                        .string("Мой профиль"));
    }

    @Test
    @DisplayName("Проверка аутентификации пользователя")
    public void correctLoginTest() throws Exception {
        ContactConfirmationPayload payload = new ContactConfirmationPayload();
        payload.setContact("stimur1709@mail.ru");
        payload.setCode("123456789");
        mockMvc.perform(
                        post("/login")
                                .content(objectMapper.writeValueAsString(payload))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(cookie().exists("token"));
    }
}