package com.example.mybookshopapp.data.entity.tag;

import com.example.mybookshopapp.data.entity.book.Book;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tags")
@Schema(description = "Сущность тэга")
public class TagBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2tag",
            joinColumns = {@JoinColumn(name = "tag_id")},
            inverseJoinColumns = {@JoinColumn(name = "book_id")})
    @JsonBackReference
    private List<Book> bookList = new ArrayList<>();
}
