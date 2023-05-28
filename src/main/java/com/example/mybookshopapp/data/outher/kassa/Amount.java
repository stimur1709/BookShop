package com.example.mybookshopapp.data.outher.kassa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Amount {
    private String value;
    private String currency;

    public Amount(String value) {
        this.value = value;
        this.currency = "RUB";
    }

}

