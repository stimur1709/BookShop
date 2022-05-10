package com.example.mybookshopapp.data.book.links.key;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class KeyBook2User implements Serializable {

    private static final int id = 1;
    private int bookId;
    private int userId;
}
