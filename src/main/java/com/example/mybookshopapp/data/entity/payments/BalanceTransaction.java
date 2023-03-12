package com.example.mybookshopapp.data.entity.payments;

import com.example.mybookshopapp.data.entity.book.Book;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "balance_transaction")
@Schema(description = "Транзакции по счетам пользователей")
public class BalanceTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id", columnDefinition = "INT NOT NULL")
    private int user;

    @Column(columnDefinition = "DATE NOT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int value;

    @Column(name = "book_id", columnDefinition = "INT")
    private Integer book;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String description;

    @Column(name = "code_payment_ex", columnDefinition = "uuid not null")
    private UUID codePaymentEx;

    @Column(name = "code_payment_in", columnDefinition = "uuid not null")
    private UUID codePaymentIn;

    @Column(name = "status_payment_id")
    private int statusPayment;

    @ManyToOne
    @JoinColumn(name = "status_payment_id", insertable = false, updatable = false)
    private StatusPayment status;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "book_id", insertable = false, updatable = false)
    @JsonBackReference
    private Book books;

    @Transient
    private String formatDate;

    public void setFormatDate(SimpleDateFormat simpleDateFormat) {
        this.formatDate = simpleDateFormat.format(time);
    }

    public BalanceTransaction(int user, int value, String codePaymentIn, String codePaymentEx) {
        this.user = user;
        this.value = value;
        this.description = "Пополнение счета";
        this.codePaymentIn = UUID.fromString(codePaymentIn);
        this.codePaymentEx = UUID.fromString(codePaymentEx);
        this.time = new Date();
        this.statusPayment = 1;
    }

    public BalanceTransaction(int user, int value, int book, UUID codePaymentIn) {
        this.user = user;
        this.value = value;
        this.book = book;
        this.description = "Покупка книги";
        this.codePaymentIn = codePaymentIn;
        this.time = new Date();
        this.statusPayment = 3;
    }

    public BalanceTransaction() {
    }
}