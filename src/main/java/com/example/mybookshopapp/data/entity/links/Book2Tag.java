package com.example.mybookshopapp.data.entity.links;

import com.example.mybookshopapp.data.entity.TagBook;
import com.example.mybookshopapp.data.entity.books.Book;
import com.example.mybookshopapp.data.entity.links.key.KeyBook2Tag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "book2tag")
public class Book2Tag {

    @EmbeddedId
    private KeyBook2Tag id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Book book;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id", insertable = false, updatable = false)
    private TagBook tag;
}
