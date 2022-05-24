package com.example.mybookshopapp.service;

import com.example.mybookshopapp.entity.book.BookEntity;
import com.example.mybookshopapp.entity.genre.GenreEntity;
import com.example.mybookshopapp.entity.tag.TagEntity;
import com.example.mybookshopapp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

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

    public Page<BookEntity> getPageOfRecommendBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<BookEntity> getPageOfRecentBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "pubDate"));
        return bookRepository.findAll(nextPage);
    }

    public Page<BookEntity> getPageOfPubDateBetweenBooks(Date from, Date to, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "pubDate"));
        return bookRepository.findBookEntityByPubDateBetween(from, to, nextPage);
    }

    public Page<BookEntity> getPageOfPopularBooks(Integer offset, Integer limit) {
        addPopularity();
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "popularity"));
        return bookRepository.findAll(nextPage);
    }

    public Page<BookEntity> getPageOfSearchResultBooks(String wordSearch, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookEntityByTitleContaining(wordSearch, nextPage);
    }

    public void addPopularity() {
        List<BookEntity> bookList = bookRepository.findAll();
        bookList.stream().filter(book ->
                booksRatingAndPopularityService.getPopularity(book.getId()).get(book.getId()) != null).forEach(book -> {
            book.setPopularity(booksRatingAndPopularityService.getPopularity(book.getId()).get(book.getId()));
            bookRepository.save(book);
        });
    }

    public Page<BookEntity> getBooksForPageTage(TagEntity tag, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.getBookByTag(tag.getSlug(), nextPage);
    }

    public Page<BookEntity> getBooksForPageGenre(GenreEntity genre, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.getBookByGenre(genre.getSlug(), nextPage);
    }
}
