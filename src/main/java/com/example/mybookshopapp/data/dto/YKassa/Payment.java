package com.example.mybookshopapp.data.dto.YKassa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Payment {
    private String id;
    private String status;
    private boolean paid;
    private Amount amount;
    @JsonProperty("authorization_details")
    private AuthorizationDetails authorizationDetails;
    @JsonProperty("created_at")
    private Date createdAt;
    private String description;
    @JsonProperty("expires_at")
    private Date expiresAt;
    private Metadata metadata;
    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;
    private Recipient recipient;
    private boolean refundable;
    private boolean test;
    @JsonProperty("income_amount")
    private IncomeAmount incomeAmount;
    private Confirmation confirmation;


}
