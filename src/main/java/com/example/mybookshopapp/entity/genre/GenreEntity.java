package com.example.mybookshopapp.entity.genre;

import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.entity.book.links.Book2GenreEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "genre")
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int parentId;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Column(columnDefinition = "SMALLINT NOT NULL DEFAULT 0")
    private int amount;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2genre",
            joinColumns = {@JoinColumn(name = "genre_id")},
            inverseJoinColumns = {@JoinColumn(name = "book_id")})
    @JsonBackReference
    private List<BookEntity> bookList = new ArrayList<>();
}
