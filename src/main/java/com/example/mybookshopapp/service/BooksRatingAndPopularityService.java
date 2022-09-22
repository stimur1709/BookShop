package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.BookRating;
import com.example.mybookshopapp.repository.BookRatingRepository;
import com.example.mybookshopapp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class BooksRatingAndPopularityService {

    private final BookRatingRepository bookRatingRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BooksRatingAndPopularityService(BookRatingRepository bookRatingRepository,
                                           BookRepository bookRepository) {
        this.bookRatingRepository = bookRatingRepository;
        this.bookRepository = bookRepository;
    }

//    public Map<Integer, Double> getPopularity(Integer bookId) {
//        List<Book2User> bookList = book2UserRepository.findBook2UserEntitiesByBookId(bookId);
//        return bookList.stream().collect(Collectors.groupingBy((Book2User b) -> b.getBook().getId(),
//                Collectors.summingDouble(((Book2User b) -> {
//                    if (b.getType().getId() == 1)
//                        return 0.4;
//                    if (b.getType().getId() == 2)
//                        return 0.7;
//                    if (b.getType().getId() == 3)
//                        return 1.0;
//                    else
//                        return 0.0;
//
//                }))));
//    }

    public void changePopularity(Book book, Double value) {
        book.setPopularity(book.getPopularity() + value);
        bookRepository.save(book);
    }

    public void changePopularity(String slug, Double value) {
        Book book = bookRepository.findBookEntityBySlug(slug);
        book.setPopularity(book.getPopularity() + value);
        bookRepository.save(book);
    }

    public double getSizeofRatingValue(int idBook, int value) {
        Optional<Book> book = bookRepository.findById(idBook);
        return book.map(bookEntity -> bookEntity.getBookRatingList()
                        .stream().filter(bookRating -> value == bookRating.getScore())
                        .findFirst().map(BookRating::getNumberOfRatings)
                        .orElse(0))
                .orElse(0);
    }

    public boolean changeRateBook(int bookId, int value) {
        Book book = bookRepository.getById(bookId);
        Optional<BookRating> bookRating = bookRatingRepository.findByBook_IdAndScore(bookId, value);
        if (bookRating.isPresent()) {
            bookRating.get().setNumberOfRatings(bookRating.get().getNumberOfRatings() + 1);
            bookRatingRepository.save(bookRating.get());
        } else {
            bookRatingRepository.save(new BookRating(value, book));
        }
        book.setRate(getRateBook(bookId));
        bookRepository.save(book);
        return true;
    }

    public double numberOfRating(int idBook) {
        return IntStream.rangeClosed(1, 5).mapToDouble(i -> getSizeofRatingValue(idBook, i)).sum();
    }

    public double getRateBook(int idBook) {
        return IntStream.rangeClosed(1, 5).mapToDouble(i -> getSizeofRatingValue(idBook, i) * i).sum()
                / numberOfRating(idBook);
    }
}
