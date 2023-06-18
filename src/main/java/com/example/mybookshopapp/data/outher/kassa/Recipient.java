package com.example.mybookshopapp.data.outher.kassa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recipient {
    @JsonProperty("account_id")
    private String accountId;
    @JsonProperty("gateway_id")
    private String gatewayId;
}
