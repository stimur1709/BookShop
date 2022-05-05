package com.example.mybookshopapp.data.book.links;

import com.example.mybookshopapp.data.author.Author;
import com.example.mybookshopapp.data.book.file.Book;
import com.example.mybookshopapp.data.book.links.key.Book2authorId;
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
    private Book2authorId book2authorId = new Book2authorId();

    @ManyToOne
    @MapsId("bookId")
    private Book book;

    @ManyToOne
    @MapsId("authorId")
    private Author author;

    @Column(columnDefinition = "INT NOT NULL  DEFAULT 0")
    private int sortIndex;
}
