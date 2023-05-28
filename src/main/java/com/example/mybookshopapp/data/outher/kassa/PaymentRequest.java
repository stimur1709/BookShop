package com.example.mybookshopapp.data.outher.kassa;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private Amount amount;
    private boolean capture;
    private Confirmation confirmation;
    private String description;

    public PaymentRequest(Amount amount, Confirmation confirmation, String description) {
        this.amount = amount;
        this.capture = true;
        this.confirmation = confirmation;
        this.description = description;
    }
}
