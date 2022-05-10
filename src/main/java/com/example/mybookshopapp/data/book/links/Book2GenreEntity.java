package com.example.mybookshopapp.data.book.links;

import com.example.mybookshopapp.data.book.BookEntity;
import com.example.mybookshopapp.data.book.links.key.KeyBook2Genre;
import com.example.mybookshopapp.data.genre.GenreEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "book2genre")
public class Book2GenreEntity {

    @EmbeddedId
    private KeyBook2Genre keyBook2Genre = new KeyBook2Genre();

    @ManyToOne
    @MapsId("bookId")
    private BookEntity book;

    @ManyToOne
    @MapsId("genreId")
    private GenreEntity genre;
}
