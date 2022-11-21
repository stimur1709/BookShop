package com.example.mybookshopapp.model.user;

import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.file.FileDownload;
import com.example.mybookshopapp.model.book.review.BookReview;
import com.example.mybookshopapp.model.book.review.BookReviewLike;
import com.example.mybookshopapp.model.book.review.Message;
import com.example.mybookshopapp.model.payments.BalanceTransaction;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@Schema(description = "Сущность пользователя")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "DATE NOT NULL")
    private Date regTime;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int balance;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String firstname;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String lastname;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<BookReview> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<BookReviewLike> reviewLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REFRESH)
    @JsonManagedReference
    private List<UserContact> userContact = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2User",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "book_id")})
    @JsonBackReference
    private List<Book> bookList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BalanceTransaction> transactionList;

    @OneToMany(mappedBy = "user")
    private List<FileDownload> downloadList;

    @OneToMany(mappedBy = "user")
    private List<Message> messageList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<UserLoginHistory> userLoginHistories;

    public User(String firstname, String lastname, String password, String hash) {
        this.hash = hash;
        this.password = password;
        this.regTime = new Date();
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User(String firstname, String lastname, String hash) {
        this.hash = hash;
        this.regTime = new Date();
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User() {
    }
}
