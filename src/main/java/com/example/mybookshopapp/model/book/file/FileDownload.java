package com.example.mybookshopapp.model.book.file;

import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "file_download")
public class FileDownload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "book_id")
    @JsonBackReference
    private Book book;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 1")
    private int count;
}
