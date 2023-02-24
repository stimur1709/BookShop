package com.example.mybookshopapp.data.entity.book;

import com.example.mybookshopapp.data.entity.book.links.key.KeyBook2User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Table(name = "books_viewed")
@Entity
@Getter
@Setter
public class BooksViewed {

    @Id
    private KeyBook2User keyBook2User;

    @Column(columnDefinition = "DATE NOT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

}
