package com.example.mybookshopapp.entity.payments;

import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.entity.user.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "balance_transaction")
@Schema(description = "Транзакции по счетам пользователей")
public class BalanceTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "user_id")
    private UserEntity user;

    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "INT NOT NULL  DEFAULT 0")
    private int value;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "book_id")
    private BookEntity book;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String description;
}
