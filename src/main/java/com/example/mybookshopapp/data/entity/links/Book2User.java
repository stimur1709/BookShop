package com.example.mybookshopapp.data.entity.links;

import com.example.mybookshopapp.data.entity.links.key.KeyBook2User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "book2user")
public class Book2User {

    @EmbeddedId
    private KeyBook2User keyBook2User;

    @Column(name = "type_id", insertable = false, updatable = false)
    private Integer type;

    @Column(name = "book_id", insertable = false, updatable = false)
    private Integer book;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Integer user;

    @UpdateTimestamp
    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDateTime time;

    public Book2User() {
    }

}