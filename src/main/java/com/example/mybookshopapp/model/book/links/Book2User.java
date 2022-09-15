package com.example.mybookshopapp.model.book.links;

import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.links.key.KeyBook2User;
import com.example.mybookshopapp.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "book2user")
public class Book2User {
    @EmbeddedId
    private KeyBook2User keyBook2User;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Book2UserType type;

    @ManyToOne
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @UpdateTimestamp
    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDateTime time;

    public Book2User(Book2UserType type, Book book, User user) {
        this.keyBook2User = new KeyBook2User(book.getId(), user.getId());
        this.time = LocalDateTime.now();
        this.type = type;
        this.book = book;
        this.user = user;
    }

    public Book2User() {
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Book2UserType getType() {
        return type;
    }

    public void setType(Book2UserType type) {
        this.type = type;
    }

    public KeyBook2User getKeyBook2User() {
        return keyBook2User;
    }

    public void setKeyBook2User(KeyBook2User keyBook2User) {
        this.keyBook2User = keyBook2User;
    }
}