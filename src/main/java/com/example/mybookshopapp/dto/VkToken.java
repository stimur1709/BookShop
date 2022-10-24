package com.example.mybookshopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VkToken {

    private String id;
    @JsonProperty("first_name")
    private String firstname;

    @JsonProperty("last_name")
    private String lastname;

    @JsonProperty("can_access_closed")
    private boolean canAccessClosed;

    @JsonProperty("is_closed")
    private boolean isClosed;

    @Override
    public String toString() {
        return "VkToken{" +
                "id='" + id + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", canAccessClosed=" + canAccessClosed +
                ", isClosed=" + isClosed +
                '}';
    }
}
