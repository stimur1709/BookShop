package com.example.mybookshopapp.data.entity;

import com.example.mybookshopapp.data.entity.books.Book;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tags")
@Schema(description = "Сущность тэга")
@NoArgsConstructor
public class TagBook extends Models {

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "book2tag",
            joinColumns = {@JoinColumn(name = "tag_id")},
            inverseJoinColumns = {@JoinColumn(name = "book_id")})
    private List<Book> bookList;

    public TagBook(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

}
