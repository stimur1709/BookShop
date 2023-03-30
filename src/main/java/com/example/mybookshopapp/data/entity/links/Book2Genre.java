package com.example.mybookshopapp.data.entity.links;

import com.example.mybookshopapp.data.entity.Genre;
import com.example.mybookshopapp.data.entity.books.Book;
import com.example.mybookshopapp.data.entity.links.key.KeyBook2Genre;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "book2genre")
public class Book2Genre {

    @EmbeddedId
    private KeyBook2Genre id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", nullable = false, insertable = false, updatable = false)
    private Book book;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "genre_id", nullable = false, insertable = false, updatable = false)
    private Genre genre;
}
