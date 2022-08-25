package com.example.mybookshopapp.model.book.links;

import com.example.mybookshopapp.model.author.Author;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.links.key.KeyBook2Author;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "book2author")
public class Book2Author {

    @EmbeddedId
    private KeyBook2Author id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Book book;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private Author author;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int sortIndex;
}
