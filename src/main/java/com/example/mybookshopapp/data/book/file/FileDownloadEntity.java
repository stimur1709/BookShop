package com.example.mybookshopapp.data.book.file;

import com.example.mybookshopapp.data.book.BookEntity;
import com.example.mybookshopapp.data.user.UserEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "file_download")
public class FileDownloadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL", name = "book_id")
    private BookEntity book;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 1")
    private int count;
}
