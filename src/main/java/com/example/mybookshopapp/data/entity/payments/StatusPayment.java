package com.example.mybookshopapp.data.entity.payments;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "status_payment")
@Getter
@Setter
public class StatusPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "status_ru", columnDefinition = "varchar(20) not null")
    private String statusRu;

    @Column(name = "status_en", columnDefinition = "varchar(20) not null")
    private String statusEn;

    @Column(columnDefinition = "varchar(255)")
    private String comment;

}
