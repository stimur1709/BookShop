package com.example.mybookshopapp.data.entity.books;

import com.example.mybookshopapp.data.entity.Models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "book_file_type")
public class BookFileType extends Models {

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "bookFileType")
    @JsonBackReference
    private List<BookFile> bookFileList;
}
