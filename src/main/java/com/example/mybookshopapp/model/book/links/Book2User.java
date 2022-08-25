package com.example.mybookshopapp.model.book.links;

import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.links.key.KeyBook2User;
import com.example.mybookshopapp.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "book2user")
public class Book2User {

    @EmbeddedId
    private KeyBook2User id;

    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDateTime time;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id", insertable = false, updatable = false)
    @JsonManagedReference
    @JsonIgnore
    private Book2UserType type;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    @JsonIgnore
    private Book book;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private User user;
}
