package com.example.mybookshopapp.data.dto.book;

import com.example.mybookshopapp.data.dto.BookFileDto;
import com.example.mybookshopapp.data.dto.BookRatingDto;
import com.example.mybookshopapp.data.dto.genre.GenreDto;
import com.example.mybookshopapp.data.dto.tag.TagBookDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
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
    private List<BookFileDto> bookFileList;
    private List<BookRatingDto> bookRatingList;
    private List<GenreDto> genreList;

}
