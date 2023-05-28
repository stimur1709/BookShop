package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.outher.ContactConfirmationPayload;
import com.example.mybookshopapp.data.entity.enums.ContactType;
import com.example.mybookshopapp.data.entity.user.UserContact;
import com.example.mybookshopapp.repository.UserContactRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

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
    private final ObjectMapper objectMapper;

    private static String mail;
    private static String phone;

    @Autowired
    UserAuthControllerTest(MockMvc mockMvc, UserContactRepository userContactRepository,
                           ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.userContactRepository = userContactRepository;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setUp() {
        mail = "stimur1794@mail.ru";
        phone = "+7 (999) 111-11-22";

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
    void correctLoginTest() throws Exception {
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