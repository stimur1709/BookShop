package com.example.mybookshopapp.data.outher.YKassa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {
    private String first6;
    private String last4;
    @JsonProperty("expiry_month")
    private String expiryMonth;
    @JsonProperty("expiry_year")
    private String expiryYear;
    @JsonProperty("card_type")
    private String cardType;
    @JsonProperty("issuer_country")
    private String issuerCountry;
    @JsonProperty("issuer_name")
    private String issuerName;
}
