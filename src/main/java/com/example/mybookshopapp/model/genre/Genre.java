package com.example.mybookshopapp.model.genre;

import com.example.mybookshopapp.model.book.Book;
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
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Genre parent;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Column(columnDefinition = "SMALLINT NOT NULL DEFAULT 0")
    private int amount;

    @OneToMany
    @JoinColumn(name = "parent_id")
    private List<Genre> childGenres;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2genre",
            joinColumns = {@JoinColumn(name = "genre_id")},
            inverseJoinColumns = {@JoinColumn(name = "book_id")})
    @JsonBackReference
    private List<Book> bookList = new ArrayList<>();

    @Override
    public String toString() {
        return "Genre{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
