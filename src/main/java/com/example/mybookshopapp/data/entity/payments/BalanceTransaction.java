package com.example.mybookshopapp.data.entity.payments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(name = "book_id", columnDefinition = "INT NOT NULL")
    private int book;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String description;

    @Column(name = "code_payment", columnDefinition = "uuid not null")
    private UUID codePayment;

    @Column(name = "status_payment_id")
    private int statusPayment;

    @ManyToOne
    @JoinColumn(name = "status_payment_id", insertable = false, updatable = false)
    private StatusPayment status;

    public BalanceTransaction(int user, int value, int book, String codePayment) {
        this.user = user;
        this.value = value;
        this.book = book;
        this.description = "Покупка книги";
        this.codePayment = UUID.fromString(codePayment);
        this.time = new Date();
        this.statusPayment = 1;
    }

    public BalanceTransaction() {
    }
}
