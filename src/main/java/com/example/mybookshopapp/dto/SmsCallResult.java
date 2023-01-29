package com.example.mybookshopapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsCallResult {

    private SmsStatus status;

    private String code;

    @JsonProperty("call_id")
    private String callId;

    private Double cost;

    private Double balance;

    @JsonProperty("status_text")
    private String statusText;
}
