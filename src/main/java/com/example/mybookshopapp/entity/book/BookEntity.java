package com.example.mybookshopapp.entity.book;

import com.example.mybookshopapp.entity.author.Author;
import com.example.mybookshopapp.entity.book.file.BookFile;
import com.example.mybookshopapp.entity.book.file.FileDownloadEntity;
import com.example.mybookshopapp.entity.book.review.BookReviewEntity;
import com.example.mybookshopapp.entity.genre.GenreEntity;
import com.example.mybookshopapp.entity.payments.BalanceTransactionEntity;
import com.example.mybookshopapp.entity.tag.TagEntity;
import com.example.mybookshopapp.entity.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "books")
@Schema(description = "Сущность книги")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "1")
    private int id;

    @Column(name = "pub_date", columnDefinition = "DATE NOT NULL")
    @Schema(example = "2010-02-02")
    private Date pubDate;

    @Column(name = "is_bestseller", columnDefinition = "SMALLINT NOT NULL")
    @Schema(example = "1")
    private int isBestseller;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @Schema(example = "anna-karenina")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @Schema(example = "Анна Каренина")
    private String title;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @Schema(example = "Картинка книги")
    private String image;

    @Column(columnDefinition = "TEXT")
    @Schema(example = "ОПисание книги")
    private String description;

    @Column(columnDefinition = "INT NOT NULL")
    @Schema(example = "1000")
    private int price;

    @Column(columnDefinition = "double precision NOT NULL DEFAULT 0")
    @Schema(example = "0.1")
    private double discount;

    @Column(columnDefinition = "double precision NOT NULL DEFAULT 0")
    @Schema(example = "0.4")
    private Double popularity;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2Author",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    @JsonManagedReference
    private List<Author> authorList = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2tag",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    @JsonManagedReference
    private List<TagEntity> tagList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<BookReviewEntity> reviewList = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2genre",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")})
    @JsonManagedReference
    private List<GenreEntity> genreList = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2User",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    @JsonManagedReference
    private List<UserEntity> userList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<BalanceTransactionEntity> transactionList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<FileDownloadEntity> downloadList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    @JsonManagedReference
    private List<BookFile> bookFileList = new ArrayList<>();

    public StringBuilder getAuthors() {
        StringBuilder authors = new StringBuilder();
        for (int i = 0; i < authorList.size(); i++) {
            authors.append(authorList.get(i).getName());
            if (authorList.size() > 1 && i != authorList.size() - 1) {
                authors.append(", ");
            }
        }
        return authors;
    }

    public Integer discountPrice() {
        if (discount == 0) {
            return price;
        } else {
            return price - Math.toIntExact(Math.round(price * discount));
        }
    }
}