package com.example.mybookshopapp.data.book.links.key;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class Book2authorId implements Serializable {
    private static final int id = 1;
    private int bookId;
    private int authorId;
}
