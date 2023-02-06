package com.example.mybookshopapp.config;

import com.example.mybookshopapp.aspect.annotation.ApiConfiguration;
import com.example.mybookshopapp.data.entity.config.Api;
import com.example.mybookshopapp.data.entity.config.Property;
import com.example.mybookshopapp.repository.ApiPropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public Api getPayment() {
        return apiPropertyRepository.findByIsMainAndProperty(true, Property.PAYMENT);
    }

}
