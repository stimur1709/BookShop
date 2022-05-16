package com.example.mybookshopapp.entity.book.file;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "book_file")
public class BookFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "type_id")
    private BookFileTypeEntity bookFileType;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String path;
}
