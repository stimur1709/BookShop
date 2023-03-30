package com.example.mybookshopapp.data.entity.books;

import com.example.mybookshopapp.data.entity.Author;
import com.example.mybookshopapp.data.entity.TagBook;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class BookF extends BooksFModels {

    private String description;

    @Column(name = "user_rating")
    private Integer userRating;

    private long count1;

    private long count2;

    private long count3;

    private long count4;

    private long count5;

    @Column(name = "rate_review")
    private int rateReview;

    @Column(name = "download_count")
    private int downloadCount;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2tag",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    @JsonManagedReference
    private List<TagBook> tagList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    @JsonManagedReference
    @OrderBy("hash")
    private List<BookFile> bookFileList;

    @OneToMany(mappedBy = "book")
    @JsonManagedReference
    private List<BookRating> bookRatingList;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2Author",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    @JsonManagedReference
    private List<Author> authorList = new ArrayList<>();

    public int discountPrice() {
        if (discount == 0) {
            return price;
        } else {
            return price - Math.toIntExact(Math.round(price * discount));
        }
    }
}
