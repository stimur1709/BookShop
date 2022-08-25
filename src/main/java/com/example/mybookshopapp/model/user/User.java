package com.example.mybookshopapp.entity.user;

import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.entity.book.file.FileDownloadEntity;
import com.example.mybookshopapp.entity.book.review.BookReviewEntity;
import com.example.mybookshopapp.entity.book.review.BookReviewLikeEntity;
import com.example.mybookshopapp.entity.book.review.MessageEntity;
import com.example.mybookshopapp.entity.payments.BalanceTransactionEntity;
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
    private String name;

    public UserEntity(String name, String password) {
        this.hash = name.replace(" ", "");
        this.password = password;
        this.regTime = LocalDateTime.now();
        this.name = name;
    }

    public UserEntity() {
    }

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<BookReview> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<BookReviewLike> reviewLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private UserContact userContact;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2User",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "book_id")})
    @JsonBackReference
    private List<Book> bookList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BalanceTransaction> transactionList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<FileDownload> downloadList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Message> messageList = new ArrayList<>();


}