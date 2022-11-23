package com.example.mybookshopapp.model.book.links.key;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class KeyBook2User implements Serializable {
    private static final long serialVersionUID = 967105892331588635L;

    @Column(name = "book_id")
    private int bookId;

    @Column(name = "user_id")
    private int userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KeyBook2User that = (KeyBook2User) o;
        return bookId == that.bookId && userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, userId);
    }

    public KeyBook2User(int bookId, int userId) {
        this.bookId = bookId;
        this.userId = userId;
    }

    public KeyBook2User() {
    }
}