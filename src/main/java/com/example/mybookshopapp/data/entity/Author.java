package com.example.mybookshopapp.data.entity;

import com.example.mybookshopapp.data.entity.books.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "authors")
@NoArgsConstructor
@ToString
public class Author extends Models {

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "book2Author",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    @ToString.Exclude
    private List<Book> bookList;

    public Author(String slug, String name, String description) {
        this.slug = slug;
        this.name = name;
        this.description = description;
    }

    public Author(Integer id, String name) {
        setId(id);
        this.name = name;
        this.slug = UUID.randomUUID().toString();
    }
}