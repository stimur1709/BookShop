package com.example.mybookshopapp.entity.book.links.key;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class KeyBook2User implements Serializable {

    @Column(name = "book_id")
    private int bookId;

    @Column(name = "user_id")
    private int userId;
}
