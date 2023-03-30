package com.example.mybookshopapp.service;

import com.example.mybookshopapp.aspect.annotation.DurationTrackable;
import com.example.mybookshopapp.data.outher.SmsCallResult;
import com.example.mybookshopapp.data.outher.SmsStatus;
import com.example.mybookshopapp.util.Generator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class SmsRuService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Generator generator;

    @Value("${sms.ru.url}")
    private String url;

    @Value("${sms.ru.apiId}")
    private String apiId;

    @Autowired
    public SmsRuService(RestTemplate restTemplate, ObjectMapper objectMapper, Generator generator) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.generator = generator;
    }

    @DurationTrackable
    public String sendSms(String phone) {
        SmsCallResult result;
        String smsRuUrl = url + apiId + "&phone=" + phone + "&ip=" + getRemoteAddress();
        try {
            result = objectMapper.readValue(restTemplate.getForObject(smsRuUrl, String.class), SmsCallResult.class);
            if (result.getCode() == null || result.getStatus().equals(SmsStatus.ERROR)) {
                log.warn(result.getStatusText());
                return generator.getSecretCode().substring(1, 6);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result.getCode().substring(0, 2) + " " + result.getCode().substring(2, 4);
    }

    private String getRemoteAddress() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
            return request.getRemoteAddr();
        }
        return null;
    }
}
