package com.example.mybookshopapp.data.dto.YKassa;

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
}
