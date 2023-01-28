package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dto.SmsCallResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Service
public class SmsService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    @Autowired
    public SmsService(RestTemplate restTemplate, ObjectMapper objectMapper, HttpServletRequest request) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public String sendSms(String phone) {
        SmsCallResult result;
        String API_ID = "01AD529F-6C73-B85F-EBAF-7B0A709E8900";
        String URL = "https://sms.ru/code/call?api_id=";
        String smsRuUrl = URL + API_ID + "&phone=" + phone + "&ip=" + request.getRemoteAddr();
        System.out.println(smsRuUrl);
        try {
            result = objectMapper.readValue(restTemplate.getForObject(smsRuUrl, String.class), SmsCallResult.class);
            System.out.println(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String s = result.getCode().substring(0, 2) + " " + result.getCode().substring(2, 4);
        System.out.println(s);
        return s;
    }
}
