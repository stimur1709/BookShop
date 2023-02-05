package com.example.mybookshopapp.data.dto.YKassa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Confirmation {
    private String type;
    @JsonProperty("return_url")
    private String returnUrl;
    @JsonProperty("confirmation_url")
    private String confirmationUrl;

    public Confirmation(String type, String returnUrl) {
        this.type = type;
        this.returnUrl = returnUrl;
    }

}

