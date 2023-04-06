package com.example.mybookshopapp.data.dto;

import com.example.mybookshopapp.data.entity.books.BookFile;
import com.example.mybookshopapp.data.entity.books.BookRating;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookFDto extends BooksFDto {

    private String description;
    private Integer userRating;
    private long count1;
    private long count2;
    private long count3;
    private long count4;
    private long count5;
    private int rateReview;
    private int downloadCount;
    private List<TagBookDto> tagList;
    private List<BookFile> bookFileList;
    private List<BookRating> bookRatingList;
    private List<GenreDto> genreList;

}
