package com.example.mybookshopapp.data.book.file;

import com.example.mybookshopapp.data.book.links.Book2AuthorEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "pub_date", columnDefinition = "DATE NOT NULL")
    private Date pubDate;

    @Column(name = "is_bestseller", columnDefinition = "SMALLINT NOT NULL")
    private int isBestseller;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String title;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String image;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "INT NOT NULL")
    private int price;

    @Column(columnDefinition = "SMALLINT NOT NULL DEFAULT 0")
    private int discount;

    @OneToMany(mappedBy = "book")
    private List<Book2AuthorEntity> authorList = new ArrayList<>();

    public StringBuilder getAuthors() {
        StringBuilder authors = new StringBuilder();
        for(int i = 0; i < authorList.size(); i++) {
            authors.append(authorList.get(i).getAuthor().getName());
            if (authorList.size() > 1 && i != authorList.size() - 1) {
                authors.append(", ");
            }
        }
        return authors;
    }
}
