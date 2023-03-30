package com.example.mybookshopapp.data.entity.payments;

import com.example.mybookshopapp.data.entity.Models;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "status_payment")
@Getter
@Setter
public class StatusPayment extends Models {

    @Column(name = "status_ru", columnDefinition = "varchar(20) not null")
    private String statusRu;

    @Column(name = "status_en", columnDefinition = "varchar(20) not null")
    private String statusEn;

    @Column(columnDefinition = "varchar(255)")
    private String comment;

}
