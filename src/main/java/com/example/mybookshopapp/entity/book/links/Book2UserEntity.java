package com.example.mybookshopapp.entity.book.links;

import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.entity.book.links.key.KeyBook2User;
import com.example.mybookshopapp.entity.user.UserEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "book2user")
public class Book2UserEntity {

    @EmbeddedId
    private KeyBook2User keyBook2User = new KeyBook2User();

    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDateTime time;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "type_id")
    private Book2UserTypeEntity typeId;

    @ManyToOne
    @MapsId("bookId")
    private BookEntity book;

    @ManyToOne
    @MapsId("userId")
    private UserEntity user;
}
