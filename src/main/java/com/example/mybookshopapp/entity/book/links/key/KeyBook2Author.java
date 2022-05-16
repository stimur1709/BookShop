package com.example.mybookshopapp.entity.book.links.key;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class KeyBook2Author implements Serializable {

    @Column(name = "book_id")
    private int bookId;

    @Column(name = "author_id")
    private int authorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyBook2Author that = (KeyBook2Author) o;
        return bookId == that.bookId && authorId == that.authorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, authorId);
    }
}
