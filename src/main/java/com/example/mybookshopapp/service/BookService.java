package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.entity.BookQuery;
import com.example.mybookshopapp.data.entity.author.Author;
import com.example.mybookshopapp.data.entity.book.Book;
import com.example.mybookshopapp.data.entity.genre.Genre;
import com.example.mybookshopapp.data.entity.tag.TagBook;
import com.example.mybookshopapp.repository.BookQueryRepository;
import com.example.mybookshopapp.repository.BookRepository;
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
    private final BookQueryRepository bookQueryRepository;
    private final UserProfileService userProfileService;

    @Autowired
    public BookService(BookRepository bookRepository, BookQueryRepository bookQueryRepository, UserProfileService userProfileService) {
        this.bookRepository = bookRepository;
        this.bookQueryRepository = bookQueryRepository;
        this.userProfileService = userProfileService;
    }

    public Page<BookQuery> getPageBooks(Integer offset, Integer limit, String properties) {
        Pageable nextPage = PageRequest.of(offset, limit, Sort.Direction.DESC, properties);
        return bookQueryRepository.getBooks(userProfileService.getUserId(), nextPage);
    }

    public Page<BookQuery> getPageOfPubDateBetweenBooks(String from, String to, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "pub_date"));
        try {
            Date dateFrom = new SimpleDateFormat("dd.MM.yyyy").parse(from);
            Date dateTo = new SimpleDateFormat("dd.MM.yyyy").parse(to);
            return bookQueryRepository.findBookEntityByPubDateBetween(userProfileService.getUserId(), dateFrom, dateTo, nextPage);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Page<BookQuery> getPageOfSearchResultBooks(String wordSearch, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookQueryRepository.findBookEntityByTitleContainingAllIgnoreCase(userProfileService.getUserId(), wordSearch, nextPage);
    }

    public Page<BookQuery> getBooksForPageTage(TagBook tag, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookQueryRepository.findByTagList_Slug(userProfileService.getUserId(), tag.getSlug(), nextPage);
    }

    public Page<BookQuery> getBooksForPageGenre(Genre genre, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookQueryRepository.getByGenreList_Slug(userProfileService.getUserId(), genre.getSlug(), nextPage);
    }

    public Page<BookQuery> getBooksForPageAuthor(Author author, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookQueryRepository.getByAuthorList_Slug(userProfileService.getUserId(), author.getSlug(), nextPage);
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

}