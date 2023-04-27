package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.book.BookFDto;
import com.example.mybookshopapp.data.dto.book.BooksFDto;
import com.example.mybookshopapp.data.entity.Image;
import com.example.mybookshopapp.data.entity.books.Book;
import com.example.mybookshopapp.data.entity.books.BookF;
import com.example.mybookshopapp.data.entity.books.BooksF;
import com.example.mybookshopapp.data.query.BookQuery;
import com.example.mybookshopapp.errors.DefaultException;
import com.example.mybookshopapp.repository.BookQueryRepository;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.repository.BooksQueryRepository;
import com.example.mybookshopapp.repository.BooksViewedRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl
        extends ModelServiceImpl<BooksF, BookQuery, BooksFDto, BookFDto, BooksQueryRepository> {


    private final BooksViewedRepository booksViewedRepository;
    private final BookQueryRepository bookQueryRepository;
    private final BookRepository bookRepository;

    @Autowired
    protected BookServiceImpl(BooksQueryRepository repository, UserProfileService userProfileService,
                              ModelMapper modelMapper, BooksViewedRepository booksViewedRepository,
                              BookQueryRepository bookQueryRepository, HttpServletRequest request, BookRepository bookRepository) {
        super(repository, BooksFDto.class, BookFDto.class, BooksF.class, userProfileService, modelMapper, request);
        this.booksViewedRepository = booksViewedRepository;
        this.bookQueryRepository = bookQueryRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Page<BooksFDto> getContents(BookQuery q) {
        PageRequest of = getPageRequest(q);
        Integer userId = userProfileService.getUserId();
        if (q.checkQuery()) {
            try {
                Date dateFrom = new SimpleDateFormat("dd.MM.yyyy").parse(q.getFrom());
                Date dateTo = new SimpleDateFormat("dd.MM.yyyy").parse(q.getTo());
                return repository.findBooks(userId, q.getSearch(), q.isBestseller(), q.isDiscount(), dateFrom, dateTo, of)
                        .map(m -> modelMapper.map(m, BooksFDto.class));
            } catch (ParseException | NullPointerException e) {
                return repository.findBooks(userId, q.getSearch(), q.isBestseller(), q.isDiscount(), new Date(userId), new Date(), of)
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
        booksViewedRepository.insertOrUpdate(book.getId(), userProfileService.getUserId());
        return modelMapper.map(book, BookFDto.class);
    }

    public List<BooksFDto> getBookUser() {
        return repository.getBooksUser(userProfileService.getUserId(), getStatusUser().toString())
                .stream()
                .map(m -> modelMapper.map(m, BooksFDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public BookFDto save(BookFDto dto) throws DefaultException {
        Book book = bookRepository.findBookEntityBySlug(dto.getSlug());
        BookF bookF = modelMapper.map(dto, BookF.class);
        book.setDescription(bookF.getDescription());
        book.setIsBestseller(bookF.getIsBestseller());
        book.setTitle(bookF.getTitle());
        book.setPrice(bookF.getPrice());
        book.setDiscount(bookF.getDiscount());
        book.setImage(new Image(bookF.getImageId()));
        book.setAuthorList(bookF.getAuthorList());
        book.setGenreList(bookF.getGenreList());
        book.setTagList(bookF.getTagList());
        bookRepository.save(book);
        return getContent(dto.getSlug());
    }
}
