package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.author.Author;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.genre.Genre;
import com.example.mybookshopapp.model.tag.TagBook;
import com.example.mybookshopapp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BooksRatingAndPopularityService booksRatingAndPopularityService;

    @Autowired
    public BookService(BookRepository bookRepository, BooksRatingAndPopularityService booksRatingAndPopularityService) {
        this.bookRepository = bookRepository;
        this.booksRatingAndPopularityService = booksRatingAndPopularityService;
    }

    public Page<Book> getPageOfRecommendBooks(Integer offset, Integer limit) {
        addRate();
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "rate"));
        return bookRepository.findAll(nextPage);
    }

    public Page<Book> getPageOfRecentBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "pubDate"));
        return bookRepository.findAll(nextPage);
    }

    public Page<Book> getPageOfPubDateBetweenBooks(String from, String to, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "pubDate"));
        try {
            Date dateFrom = new SimpleDateFormat("dd.MM.yyyy").parse(from);
            Date dateTo = new SimpleDateFormat("dd.MM.yyyy").parse(to);
            return bookRepository.findBookEntityByPubDateBetween(dateFrom, dateTo, nextPage);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Page<Book> getPageOfPopularBooks(Integer offset, Integer limit) {
        addPopularity();
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "popularity"));
        return bookRepository.findAll(nextPage);
    }

    public Page<Book> getPageOfSearchResultBooks(String wordSearch, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookEntityByTitleContaining(wordSearch, nextPage);
    }

    private void addPopularity() {
        List<Book> bookList = bookRepository.findAll();
        bookList.stream().filter(book -> booksRatingAndPopularityService.getPopularity(book.getId()).get(book.getId()) != null).forEach(book -> {
            book.setPopularity(booksRatingAndPopularityService.getPopularity(book.getId()).get(book.getId()));
            bookRepository.save(book);
        });
    }

    private void addRate() {
        bookRepository.findAll().forEach(book -> {
            book.setRate(booksRatingAndPopularityService.getRateBook(book.getId()));
            bookRepository.save(book);
        });
    }

    public Page<Book> getBooksForPageTage(TagBook tag, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findByTagList_Slug(tag.getSlug(), nextPage);
    }

    public Page<Book> getBooksForPageGenre(Genre genre, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.getByGenreList_Slug(genre.getSlug(), nextPage);
    }

    public Page<Book> getBooksForPageAuthor(Author author, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.getByAuthorList_Id(author.getId(), nextPage);
    }

    public Book getBookBySlug(String slug) {
        return bookRepository.findBookEntityBySlug(slug);
    }

    public long getNumbersOffAllBooks() {
        return bookRepository.count();
    }

    public void save(Book bookToUpdate) {
        bookRepository.save(bookToUpdate);
    }

    public List<Book> getBooksFromCookie(String contents) {
        if (contents != null) {
            contents = contents.startsWith("/") ? contents.substring(1) : contents;
            contents = contents.endsWith("/") ? contents.substring(0, contents.length() - 1) : contents;
            String[] cookieSlugs = contents.split("/");
            return bookRepository.findBookEntitiesBySlugIn(List.of(cookieSlugs));
        }
        return Collections.emptyList();
    }
}