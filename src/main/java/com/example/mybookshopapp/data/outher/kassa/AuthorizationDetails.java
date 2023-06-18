package com.example.mybookshopapp.data.outher.kassa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizationDetails {
    private String rrn;
    @JsonProperty("auth_code")
    private String authCode;
    @JsonProperty("three_d_secure")
    private ThreeDSecure threeDSecure;
}
