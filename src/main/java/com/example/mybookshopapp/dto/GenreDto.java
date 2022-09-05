package com.example.mybookshopapp.dto;

import com.example.mybookshopapp.model.book.Book;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(description = "Модель жанра")
public class GenreDto implements Comparable<GenreDto> {

    private int id;

    private int parentId;

    private String slug;

    private String name;

    private int amount;

    private List<Book> bookList = new ArrayList<>();

    @Override
    public String toString() {
        return "GenreDto{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }

    @Override
    public int compareTo(GenreDto o) {
        return Integer.compare(o.getAmount(), this.getAmount());
    }

}
