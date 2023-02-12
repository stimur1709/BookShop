package com.example.mybookshopapp.data.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class Balance {

    @Min(value = 1, message = "Сумма должна состоять из цифр больше нуля")
    private Integer sum;

    public Balance() {
    }
}
