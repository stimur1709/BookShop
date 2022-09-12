package com.example.mybookshopapp.model.book.links;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "book2user")
public class Book2User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDateTime time;

    @Column(name = "type_id", columnDefinition = "INT NOT NULL")
    private Integer typeId;

    @Column(name = "book_id", columnDefinition = "INT NOT NULL")
    private Integer bookId;

    @Column(name = "user_id", columnDefinition = "INT NOT NULL")
    private Integer userId;

    public Book2User(Integer typeId, Integer bookId, Integer userId) {
        this.time = LocalDateTime.now();
        this.typeId = typeId;
        this.bookId = bookId;
        this.userId = userId;
    }

    public Book2User() {
    }
}
