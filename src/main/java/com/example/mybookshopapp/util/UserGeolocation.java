package com.example.mybookshopapp.util;

import io.ipgeolocation.api.Geolocation;
import io.ipgeolocation.api.GeolocationParams;
import io.ipgeolocation.api.IPGeolocationAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserGeolocation {

    private final IPGeolocationAPI ipGeolocationAPI;
    private final GeolocationParams geolocationParams;

    @Autowired
    public UserGeolocation(IPGeolocationAPI ipGeolocationAPI, GeolocationParams geolocationParams) {
        this.ipGeolocationAPI = ipGeolocationAPI;
        this.geolocationParams = geolocationParams;
    }

    public Geolocation getGeolocation() {
        geolocationParams.setIPAddress("91.201.75.44");
        return ipGeolocationAPI.getGeolocation(geolocationParams);
    }
}
