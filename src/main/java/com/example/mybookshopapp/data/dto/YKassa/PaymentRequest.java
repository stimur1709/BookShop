package com.example.mybookshopapp.data.dto.YKassa;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    public Amount amount;
    public boolean capture;
    public Confirmation confirmation;
    public String description;

    public PaymentRequest(Amount amount, Confirmation confirmation, String description) {
        this.amount = amount;
        this.capture = true;
        this.confirmation = confirmation;
        this.description = description;
    }
}
