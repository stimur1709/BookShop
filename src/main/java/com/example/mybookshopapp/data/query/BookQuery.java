package com.example.mybookshopapp.data.query;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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

    public BookQuery(Integer offset, Integer limit, String property, boolean reverse, String search) {
        super(offset, limit, property, reverse, search);
    }

    @Override
    public boolean checkQuery() {
        return super.checkQuery() || discount || bestseller || from != null || to != null;
    }
}
