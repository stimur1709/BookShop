package com.example.mybookshopapp.data.entity;

import com.example.mybookshopapp.data.entity.links.key.KeyBook2User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

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
