package com.example.mybookshopapp.data.outher.YKassa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentMethod {
    private String type;
    private String id;
    private boolean saved;
    private Card card;
    private String title;
    @JsonProperty("account_number")
    public String accountNumber;

}
