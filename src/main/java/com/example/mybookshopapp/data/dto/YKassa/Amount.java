package com.example.mybookshopapp.data.dto.YKassa;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Amount {
    private String value;
    private String currency;

    public Amount(String value) {
        this.value = value;
        this.currency = "RUB";
    }

    public Amount() {
    }
}

