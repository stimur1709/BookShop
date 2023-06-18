package com.example.mybookshopapp.data.outher;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
public class Balance {

    @Min(value = 1, message = "Сумма должна состоять из цифр больше нуля")
    private Integer sum;

}
