package com.example.mybookshopapp.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Query {

    private Integer offset;
    private Integer limit;
    private boolean reverse;
    private String search;
    private String property;

    public Query(Integer offset, Integer limit, String property, boolean reverse) {
        this.offset = offset;
        this.limit = limit;
        this.reverse = reverse;
        this.property = property;
    }

    public Query(Integer offset, Integer limit, String property) {
        this.offset = offset;
        this.limit = limit;
        this.property = property;
    }

    public Query() {
    }

    public String getSearch() {
        return search == null ? "" : search;
    }
}
