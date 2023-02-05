package com.example.mybookshopapp.config;

import com.example.mybookshopapp.aspect.annotation.ApiConfiguration;
import com.example.mybookshopapp.data.entity.config.Api;
import com.example.mybookshopapp.data.entity.config.Property;
import com.example.mybookshopapp.repository.ApiPropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Configuration
@Slf4j
public class PaymentConfig {

    private final ApiPropertyRepository apiPropertyRepository;

    @Autowired
    public PaymentConfig(ApiPropertyRepository apiPropertyRepository) {
        this.apiPropertyRepository = apiPropertyRepository;
    }

    @Bean
    @ApiConfiguration
    public Map<String, Object> getPayment() {
        final Api api = apiPropertyRepository.findByIsMainAndProperty(true, Property.PAYMENT);
        if (api == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(api.getUsername(), api.getApiKey());
        headers.add("Idempotence-Key", UUID.randomUUID().toString());
        return Map.of("url", api.getUrl(), "headers", headers);
    }

}
