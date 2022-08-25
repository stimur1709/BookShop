package com.example.mybookshopapp.model.book;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class BookRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int score;

    @Column(name = "number_of_ratings", columnDefinition = "INT NOT NULL DEFAULT 0")
    private int numberOfRatings;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    @JsonBackReference
    private Book book;
}
