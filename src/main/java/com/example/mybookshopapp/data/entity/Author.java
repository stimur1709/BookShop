package com.example.mybookshopapp.data.entity;

import com.example.mybookshopapp.data.entity.books.Book;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "authors")
public class Author extends Models {

    @Column(columnDefinition = "VARCHAR(255)")
    private String photo;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String description;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "book2Author",
            joinColumns = {@JoinColumn(name = "author_id")},
            inverseJoinColumns = {@JoinColumn(name = "book_id")})
    @JsonBackReference
    private List<Book> bookList = new ArrayList<>();

}