package com.example.mybookshopapp.data.query;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BTQuery extends Query {

    private Integer userId;
    private Integer bookId;
    private List<Integer> statuses;
    private boolean auth;
    private String interval;
    private String dateS;
    private String dateE;

    public BTQuery(Integer offset, Integer limit, String property, Integer userId, List<Integer> statuses) {
        super(offset, limit, property);
        this.userId = userId;
        this.statuses = statuses;
    }
}
