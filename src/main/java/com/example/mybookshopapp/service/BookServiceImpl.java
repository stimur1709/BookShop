package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.book.BookFDto;
import com.example.mybookshopapp.data.dto.book.BooksFDto;
import com.example.mybookshopapp.data.entity.books.Book;
import com.example.mybookshopapp.data.entity.books.BookF;
import com.example.mybookshopapp.data.entity.books.BooksF;
import com.example.mybookshopapp.data.query.BookQuery;
import com.example.mybookshopapp.errors.DefaultException;
import com.example.mybookshopapp.errors.RestDefaultException;
import com.example.mybookshopapp.repository.BookQueryRepository;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.repository.BooksQueryRepository;
import com.example.mybookshopapp.repository.BooksViewedRepository;
import com.example.mybookshopapp.service.user.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookServiceImpl
        extends ModelServiceImpl<BooksF, BookQuery, BooksFDto, BookFDto, BooksQueryRepository> {


    private final BooksViewedRepository booksViewedRepository;
    private final BookQueryRepository bookQueryRepository;
    private final BookRepository bookRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    protected BookServiceImpl(BooksQueryRepository repository, UserProfileService userProfileService,
                              ModelMapper modelMapper, BooksViewedRepository booksViewedRepository,
                              BookQueryRepository bookQueryRepository, HttpServletRequest request, BookRepository bookRepository, EntityManager entityManager) {
        super(repository, BooksFDto.class, BookFDto.class, BooksF.class, userProfileService, modelMapper, request);
        this.booksViewedRepository = booksViewedRepository;
        this.bookQueryRepository = bookQueryRepository;
        this.bookRepository = bookRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Page<BooksFDto> getPageContents(BookQuery q) {
        PageRequest of = getPageRequest(q);
        Integer userId = userProfileService.getUserId();
        if (q.checkQuery()) {
            try {
                Date dateFrom = new SimpleDateFormat("dd.MM.yyyy").parse(q.getFrom());
                Date dateTo = new SimpleDateFormat("dd.MM.yyyy").parse(q.getTo());
                return repository.findBooks(userId, q.getSearch(), q.isBestseller(), q.isDiscount(), dateFrom, dateTo, q.getIds(), of)
                        .map(m -> modelMapper.map(m, BooksFDto.class));
            } catch (ParseException | NullPointerException e) {
                return repository.findBooks(userId, q.getSearch(), q.isBestseller(), q.isDiscount(), new Date(userId), new Date(), q.getIds(), of)
                        .map(m -> modelMapper.map(m, BooksFDto.class));
            }
        } else if (q.getProperty() == null) {
            return repository.getBooks(userId, of)
                    .map(m -> modelMapper.map(m, BooksFDto.class));
        } else {
            switch (q.getProperty()) {
                case "genre":
                    return repository.getBooksByGenreSlug(userId, q.getSlug(), PageRequest.of(q.getOffset(), q.getLimit()))
                            .map(m -> modelMapper.map(m, BooksFDto.class));
                case "author":
                    return repository.getBooksByAuthorSlug(userId, q.getSlug(), PageRequest.of(q.getOffset(), q.getLimit()))
                            .map(m -> modelMapper.map(m, BooksFDto.class));
                case "tag":
                    return repository.getBooksByTagSlug(userId, q.getSlug(), PageRequest.of(q.getOffset(), q.getLimit()))
                            .map(m -> modelMapper.map(m, BooksFDto.class));
                case "viewed":
                    return repository.getBooksRecentlyViewed(userId, PageRequest.of(q.getOffset(), q.getLimit()))
                            .map(m -> modelMapper.map(m, BooksFDto.class));
                case "recommend":
                    return repository.getRecommendedBooks(userId, PageRequest.of(q.getOffset(), q.getLimit()))
                            .map(m -> modelMapper.map(m, BooksFDto.class));
                default:
                    return repository.getBooks(userId, of)
                            .map(m -> modelMapper.map(m, BooksFDto.class));
            }
        }
    }

    @Override
    public BookFDto getContent(String slug) {
        BookF book = bookQueryRepository.getBook(userProfileService.getUserId(), slug);
        if (book == null) {
            return new BookFDto();
        } else {
            booksViewedRepository.insertOrUpdate(book.getId(), userProfileService.getUserId());
            return modelMapper.map(book, BookFDto.class);
        }
    }

    public List<BooksFDto> getBookUser() {
        return repository.getBooksUser(userProfileService.getUserId(), getStatusUser().toString())
                .stream()
                .map(m -> modelMapper.map(m, BooksFDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookFDto save(BookFDto dto) throws DefaultException {
        if (dto.getId() == null) {
            Book save = bookRepository.save(entityManager.merge(modelMapper.map(dto, Book.class)));
            return modelMapper.map(save, BookFDto.class);
        } else {
            Book book = modelMapper.map(dto, Book.class);
            bookRepository.save(book);
            return getContent(dto.getSlug());
        }
    }

    @Override
    public void delete(int id) throws RestDefaultException, DataAccessException {
        try {
            bookRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            log.error(ex.getMessage());
            throw new RestDefaultException("Не существует имеется связь с пользователем");
        }
    }
}
