package com.example.mybookshopapp.entity.book.links;

import com.example.mybookshopapp.entity.author.Author;
import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.entity.book.links.key.KeyBook2Author;
import com.example.mybookshopapp.entity.book.links.key.KeyBook2Tag;
import com.example.mybookshopapp.entity.tag.TagEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "book2tag")
public class Book2TagEntity {

    @EmbeddedId
    private KeyBook2Tag id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private BookEntity book;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id", insertable = false, updatable = false)
    private TagEntity tag;
}
