package com.example.mybookshopapp.data.outher.litres;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Labels {

    @JsonProperty("is_bestseller")
    private boolean isBestseller;
}
