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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@Schema(description = "Сущность пользователя")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    private String password;

    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDateTime regTime;

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

    @OneToMany(mappedBy = "user")
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

    public User(String firstname, String lastname, String password) {
        this.hash = firstname.replace(" ", "") + lastname.replace(" ", "");
        this.password = password;
        this.regTime = LocalDateTime.now();
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User(String firstname, String lastname) {
        this.hash = firstname.replace(" ", "") + lastname.replace(" ", "");
        this.regTime = LocalDateTime.now();
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User() {
    }
}
