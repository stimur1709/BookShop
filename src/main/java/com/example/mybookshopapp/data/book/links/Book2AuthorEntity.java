package com.example.mybookshopapp.data.book.links;

import com.example.mybookshopapp.data.author.Author;
import com.example.mybookshopapp.data.book.BookEntity;
import com.example.mybookshopapp.data.book.links.key.KeyBook2Author;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "book2author")
public class Book2AuthorEntity {

    @EmbeddedId
    private KeyBook2Author keyBook2Author = new KeyBook2Author();

    @ManyToOne
    @MapsId("bookId")
    private BookEntity book;

    @ManyToOne
    @MapsId("authorId")
    private Author author;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int sortIndex;
}
