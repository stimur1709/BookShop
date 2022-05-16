package com.example.mybookshopapp.data.book;

import com.example.mybookshopapp.data.author.Author;
import com.example.mybookshopapp.data.book.file.FileDownloadEntity;
import com.example.mybookshopapp.data.book.links.Book2GenreEntity;
import com.example.mybookshopapp.data.book.links.Book2UserEntity;
import com.example.mybookshopapp.data.book.review.BookReviewEntity;
import com.example.mybookshopapp.data.payments.BalanceTransactionEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "pub_date", columnDefinition = "DATE NOT NULL")
    private Date pubDate;

    @Column(name = "is_bestseller", columnDefinition = "SMALLINT NOT NULL")
    private int isBestseller;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String title;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String image;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "INT NOT NULL")
    private int price;

    @Column(columnDefinition = "SMALLINT NOT NULL DEFAULT 0")
    private int discount;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2Author",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    @JsonManagedReference
    private List<Author> authorList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<BookReviewEntity> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<Book2GenreEntity> genreList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<Book2UserEntity> userList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<BalanceTransactionEntity> transactionList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<FileDownloadEntity> downloadList = new ArrayList<>();


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
}