package com.example.mybookshopapp.data.entity;

import com.example.mybookshopapp.data.entity.books.Book;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "genre")
@NoArgsConstructor
public class Genre extends Models {

    @ManyToOne
    @JsonManagedReference
    private Genre parent;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @OneToMany
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private List<Genre> childGenres;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "book2genre",
            joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> bookList;

    public Genre(String name, String slug) {
        this.slug = slug;
        this.name = name;
        Genre genre = new Genre();
        genre.setId(1);
        setParent(genre);
    }

}
