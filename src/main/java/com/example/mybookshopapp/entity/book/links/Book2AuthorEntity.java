package com.example.mybookshopapp.entity.book.links;

import com.example.mybookshopapp.entity.author.Author;
import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.entity.book.links.key.KeyBook2Author;
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
    private KeyBook2Author id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private BookEntity book;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private Author author;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int sortIndex;
}
