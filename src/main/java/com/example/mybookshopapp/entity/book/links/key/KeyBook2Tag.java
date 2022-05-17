package com.example.mybookshopapp.entity.book.links.key;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class KeyBook2Tag implements Serializable {

    @Column(name = "book_id")
    private int bookId;

    @Column(name = "tag_id")
    private int tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyBook2Tag that = (KeyBook2Tag) o;
        return bookId == that.bookId && tagId == that.tagId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, tagId);
    }
}
