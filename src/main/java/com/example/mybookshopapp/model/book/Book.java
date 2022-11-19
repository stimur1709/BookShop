package com.example.mybookshopapp.model.book;

import com.example.mybookshopapp.model.author.Author;
import com.example.mybookshopapp.model.book.file.BookFile;
import com.example.mybookshopapp.model.book.file.FileDownload;
import com.example.mybookshopapp.model.book.review.BookReview;
import com.example.mybookshopapp.model.genre.Genre;
import com.example.mybookshopapp.model.payments.BalanceTransaction;
import com.example.mybookshopapp.model.tag.TagBook;
import com.example.mybookshopapp.model.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "books")
@Schema(description = "Сущность книги")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "1")
    private int id;

    @Column(name = "pub_date", columnDefinition = "DATE NOT NULL")
    @Schema(example = "2010-02-02")
    private Date pubDate;

    @Column(name = "is_bestseller", columnDefinition = "SMALLINT NOT NULL")
    @Schema(example = "1")
    private int isBestseller;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @Schema(example = "anna-karenina")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @Schema(example = "Анна Каренина")
    private String title;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @Schema(example = "Картинка книги")
    private String image;

    @Column(columnDefinition = "TEXT")
    @Schema(example = "ОПисание книги")
    private String description;

    @Column(columnDefinition = "INT NOT NULL")
    @Schema(example = "1000")
    private int price;

    @Column(columnDefinition = "double precision NOT NULL DEFAULT 0")
    @Schema(example = "0.1")
    private double discount;

    @Column(columnDefinition = "double precision NOT NULL DEFAULT 0")
    @Schema(example = "0.4")
    private Double popularity;

    @Formula("(select coalesce(avg(br.rating), 0) from book_rating br where br.book_id = id)")
    private double rate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2Author",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    @JsonManagedReference
    private List<Author> authorList = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2tag",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    @JsonManagedReference
    private List<TagBook> tagList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    @JsonManagedReference
    private List<BookReview> reviewList = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "book2genre",
            joinColumns = {@JoinColumn(name = "book_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id", referencedColumnName = "id")})
    @JsonManagedReference
    private List<Genre> genreList = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book2User",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    @JsonManagedReference
    private List<User> userList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    @JsonManagedReference
    private List<BalanceTransaction> transactionList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    @JsonManagedReference
    private List<FileDownload> downloadList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    @JsonManagedReference
    private List<BookFile> bookFileList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    @JsonManagedReference
    private List<BookRating> bookRatingList = new ArrayList<>();

    public String getAuthors() {
        return getAuthorList().size() > 1 ? getAuthorList().get(0).getName() + " и другие" : getAuthorList().get(0).getName();
    }

    public int discount() {
        return (int) (discount * 100);
    }

    public int discountPrice() {
        if (discount == 0) {
            return price;
        } else {
            return price - Math.toIntExact(Math.round(price * discount));
        }
    }
}