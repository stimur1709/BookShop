package com.example.mybookshopapp.data.entity.books;

import com.example.mybookshopapp.data.entity.Models;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "book_file")
public class BookFile extends Models {

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "type_id")
    @JsonManagedReference
    private BookFileType bookFileType;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String path;

    @Column(name = "book_id")
    private Integer bookId;
}
