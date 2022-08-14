package com.example.mybookshopapp.service;

import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.entity.book.BookRating;
import com.example.mybookshopapp.entity.book.links.Book2UserEntity;
import com.example.mybookshopapp.repository.Book2UserRepository;
import com.example.mybookshopapp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BooksRatingAndPopularityService {

    private final Book2UserRepository book2UserRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BooksRatingAndPopularityService(Book2UserRepository book2UserRepository,
                                           BookRepository bookRepository) {
        this.book2UserRepository = book2UserRepository;
        this.bookRepository = bookRepository;
    }

    public Map<Integer, Double> getPopularity(Integer bookId) {
        List<Book2UserEntity> bookList = book2UserRepository.findBook2UserEntitiesByBookId(bookId);
        return bookList.stream().collect(Collectors.groupingBy((Book2UserEntity b) -> b.getBook().getId(),
                Collectors.summingDouble(((Book2UserEntity b) -> {
                    if (b.getType().getId() == 1) {
                        return 0.4;
                    }
                    if (b.getType().getId() == 2) {
                        return 0.7;
                    }
                    if (b.getType().getId() == 3) {
                        return 1.0;
                    } else {
                        return 0.0;
                    }
                }))));
    }

    public void changePopularity(String slug, String cookieName, boolean isPopularity) {
        BookEntity book = bookRepository.findBookEntityBySlug(slug);
        switch (cookieName) {
            case ("keptContents"):
                System.out.println(1 + slug);
                book.setPopularity(isPopularity ? book.getPopularity() + 0.4 : book.getPopularity() - 0.4);
                break;
            case ("cartContents"):
                System.out.println(2 + slug);
                book.setPopularity(isPopularity ? book.getPopularity() + 0.7 : book.getPopularity() - 0.7);
                break;
        }
        bookRepository.save(book);
    }

    public double getSizeofRatingValue(int idBook, int value) {
        Optional<BookEntity> book = bookRepository.findById(idBook);
        return book.map(bookEntity -> bookEntity.getBookRatingList()
                        .stream().filter(bookRating -> value == bookRating.getScore())
                        .findFirst().map(BookRating::getNumberOfRatings)
                        .orElse(0))
                .orElse(0);
    }

    public void changeRateBook(int bookId, int value) {
        Optional<BookEntity> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            book.get().getBookRatingList()
                    .stream().filter(bookRating -> bookRating.getScore() == value)
                    .forEach(bookRating -> bookRating.setNumberOfRatings(bookRating.getNumberOfRatings() + 1));
            bookRepository.save(book.get());
        }
    }

    public double numberOfRating(int idBook) {
        return IntStream.rangeClosed(1, 5).mapToDouble(i -> getSizeofRatingValue(idBook, i)).sum();
    }

    public long getRateBook(int idBook) {
        return Math.round(IntStream.rangeClosed(1, 5).mapToDouble(i -> getSizeofRatingValue(idBook, i) * i).sum()
                / numberOfRating(idBook));
    }
}
