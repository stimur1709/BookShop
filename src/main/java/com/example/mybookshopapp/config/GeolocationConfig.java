package com.example.mybookshopapp.config;

import io.ipgeolocation.api.GeolocationParams;
import io.ipgeolocation.api.IPGeolocationAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeolocationConfig {

    @Value("${geolocation.key}")
    private String geolocationKey;

    @Bean
    public IPGeolocationAPI getIPGeolocationAPI() {
        return new IPGeolocationAPI(geolocationKey);
    }

    @Bean
    public GeolocationParams getGeolocationParams() {
        return new GeolocationParams();
    }

}
