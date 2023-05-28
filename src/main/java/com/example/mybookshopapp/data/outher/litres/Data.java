package com.example.mybookshopapp.data.outher.litres;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Data {

    private String type;
    private Instance instance;
    private String value;
    @JsonProperty("documents_count")
    private int documentsCount;
    @JsonProperty("is_selected")
    private boolean isSelected;

}
