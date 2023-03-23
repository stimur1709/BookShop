package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.entity.BookQuery;
import com.example.mybookshopapp.data.entity.BooksQuery;
import com.example.mybookshopapp.data.entity.author.Author;
import com.example.mybookshopapp.data.entity.book.Book;
import com.example.mybookshopapp.data.entity.genre.Genre;
import com.example.mybookshopapp.data.entity.tag.TagBook;
import com.example.mybookshopapp.repository.BookQueryRepository;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.repository.BooksQueryRepository;
import com.example.mybookshopapp.repository.BooksViewedRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BooksQueryRepository booksQueryRepository;
    private final UserProfileService userProfileService;
    private final BookQueryRepository bookQueryRepository;
    private final BooksViewedRepository booksViewedRepository;

    @Autowired
    public BookService(BookRepository bookRepository, BooksQueryRepository booksQueryRepository, UserProfileService userProfileService,
                       BookQueryRepository bookQueryRepository, BooksViewedRepository booksViewedRepository) {
        this.bookRepository = bookRepository;
        this.booksQueryRepository = booksQueryRepository;
        this.userProfileService = userProfileService;
        this.bookQueryRepository = bookQueryRepository;
        this.booksViewedRepository = booksViewedRepository;
    }

    public Page<BooksQuery> getPageBooks(Integer offset, Integer limit, String properties, boolean reverse) {
        switch (properties) {
            case "viewed":
                return booksQueryRepository.getBooksRecentlyViewed(userProfileService.getUserId(), PageRequest.of(offset, limit));
            case "recommend":
                return booksQueryRepository.getRecommendedBooks(userProfileService.getUserId(), PageRequest.of(offset, limit));
            default:
                return booksQueryRepository.getBooks(userProfileService.getUserId(), PageRequest.of(offset, limit, !reverse ? Sort.Direction.DESC : Sort.Direction.ASC, properties));
        }
    }

    public Page<BooksQuery> getPageBooks(Integer offset, Integer limit, String properties, boolean reverse, String to, String from, boolean bestseller, boolean discount, String search) {
        if (bestseller || discount || !search.isBlank() || (from != null && to != null)) {
            PageRequest of = PageRequest.of(offset, limit, Sort.by(!reverse ? Sort.Direction.ASC : Sort.Direction.DESC, properties));
            try {
                Date dateFrom = new SimpleDateFormat("dd.MM.yyyy").parse(from);
                Date dateTo = new SimpleDateFormat("dd.MM.yyyy").parse(to);
                return booksQueryRepository.findBooks(userProfileService.getUserId(), search, bestseller, discount, dateFrom, dateTo, of);
            } catch (NullPointerException | ParseException ex) {
                return booksQueryRepository.findBooks(userProfileService.getUserId(), search, bestseller, discount, new Date(1), new Date(), of);
            }
        } else {
            return getPageBooks(offset, limit, properties, reverse);
        }
    }

    public Page<BooksQuery> getPageOfSearchResultBooks(String wordSearch, Pageable page) {
        return booksQueryRepository.findBooks(userProfileService.getUserId(), wordSearch, false, false, new Date(1), new Date(), page);
    }

    public Page<BooksQuery> getBooksForPageTage(TagBook tag, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return booksQueryRepository.findByTagList_Slug(userProfileService.getUserId(), tag.getSlug(), nextPage);
    }

    public Page<BooksQuery> getBooksForPageGenre(Genre genre, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return booksQueryRepository.getByGenreList_Slug(userProfileService.getUserId(), genre.getSlug(), nextPage);
    }

    public Page<BooksQuery> getBooksForPageAuthor(Author author, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return booksQueryRepository.getByAuthorList_Slug(userProfileService.getUserId(), author.getSlug(), nextPage);
    }

    public Book getBookBySlug(String slug) {
        return bookRepository.findBookEntityBySlug(slug);
    }

    public BookQuery getBookQBySlug(String slug) {
        return bookQueryRepository.findBookEntityBySlug(userProfileService.getUserId(), slug);
    }

    public void save(Book bookToUpdate) {
        bookRepository.save(bookToUpdate);
    }

    public void saveBooksViewed(int bookId) {
        booksViewedRepository.insertOrUpdate(bookId, userProfileService.getUserId());
    }

}