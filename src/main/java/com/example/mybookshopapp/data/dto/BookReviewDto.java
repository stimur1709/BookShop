package com.example.mybookshopapp.data.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class BookReviewDto extends Dto {

    private Integer bookId;
    private Integer userId;
    private Date time;
    @NotBlank(message = "{message.reviewEmpty}")
    private String text;
    private String name;
    private String date;
    private short value;
    private long likes;
    private long dislikes;
    private short status;
}
