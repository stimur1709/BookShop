package com.example.mybookshopapp.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookQuery extends Query {

    private String from;
    private String to;
    private String slug;
    private boolean bestseller;
    private boolean discount;

    public BookQuery(Integer offset, Integer limit, String property, boolean reverse) {
        super(offset, limit, property, reverse);
    }

    public BookQuery(Integer offset, Integer limit, String property, String slug) {
        super(offset, limit, property);
        this.slug = slug;
    }

    public BookQuery() {
    }

}
