package com.example.mybookshopapp.entity.user;

import com.example.mybookshopapp.entity.book.file.FileDownloadEntity;
import com.example.mybookshopapp.entity.book.links.Book2UserEntity;
import com.example.mybookshopapp.entity.book.review.BookReviewEntity;
import com.example.mybookshopapp.entity.book.review.BookReviewLikeEntity;
import com.example.mybookshopapp.entity.book.review.MessageEntity;
import com.example.mybookshopapp.entity.payments.BalanceTransactionEntity;
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
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDateTime regTime;

    @Column(columnDefinition = "INT NOT NULL")
    private int balance;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @OneToMany(mappedBy = "user")
    private List<BookReviewEntity> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BookReviewLikeEntity> reviewLikeList = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private UserContactEntity userContact;

    @OneToMany(mappedBy = "user")
    private List<Book2UserEntity> bookList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BalanceTransactionEntity> transactionList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<FileDownloadEntity> downloadList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<MessageEntity> messageList = new ArrayList<>();
}
