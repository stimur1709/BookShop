package com.example.mybookshopapp.entity.book.links.key;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class KeyBook2Genre implements Serializable {

    @Column(name = "book_id")
    private int bookId;

    @Column(name = "genre_id")
    private int genreId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyBook2Genre that = (KeyBook2Genre) o;
        return bookId == that.bookId && genreId == that.genreId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, genreId);
    }
}
