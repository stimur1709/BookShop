package com.example.mybookshopapp.data.entity.book.file;

import com.example.mybookshopapp.data.entity.book.links.key.KeyBook2User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "file_download")
public class FileDownload {

    @EmbeddedId
    private KeyBook2User keyBook2User;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 1")
    private int count;
}
