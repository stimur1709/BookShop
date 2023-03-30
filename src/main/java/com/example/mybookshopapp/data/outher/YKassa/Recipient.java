package com.example.mybookshopapp.data.outher.YKassa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recipient {
    @JsonProperty("account_id")
    private String account_id;
    @JsonProperty("gateway_id")
    private String gateway_id;
}
