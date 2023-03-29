package com.example.mybookshopapp.service.news;

import com.example.mybookshopapp.data.dto.BookFDto;
import com.example.mybookshopapp.data.dto.BookQuery;
import com.example.mybookshopapp.data.dto.BooksFDto;
import com.example.mybookshopapp.data.entity.news.BookF;
import com.example.mybookshopapp.data.entity.news.BooksF;
import com.example.mybookshopapp.repository.BooksViewedRepository;
import com.example.mybookshopapp.repository.news.BookQueryRepository;
import com.example.mybookshopapp.repository.news.BooksQueryRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BookServiceImpl extends ModelServiceImpl<BooksF, BookQuery, BooksFDto, BooksQueryRepository> {


    private final BooksViewedRepository booksViewedRepository;
    private final BookQueryRepository bookQueryRepository;

    @Autowired
    protected BookServiceImpl(BooksQueryRepository repository, UserProfileService userProfileService, ModelMapper modelMapper,
                              BooksViewedRepository booksViewedRepository, BookQueryRepository bookQueryRepository) {
        super(repository, BooksFDto.class, userProfileService, modelMapper);
        this.booksViewedRepository = booksViewedRepository;
        this.bookQueryRepository = bookQueryRepository;
    }

    @Override
    public Page<BooksFDto> getContents(BookQuery q) {
        PageRequest of = getPageRequest(q);
        Integer userId = userProfileService.getUserId();
        if (q.isBestseller() || q.isDiscount() || !q.getSearch().isBlank() || q.getFrom() != null || q.getTo() != null) {
            try {
                Date dateFrom = new SimpleDateFormat("dd.MM.yyyy").parse(q.getFrom());
                Date dateTo = new SimpleDateFormat("dd.MM.yyyy").parse(q.getTo());
                return repository.findBooks(userId, q.getSearch(), q.isBestseller(), q.isDiscount(), dateFrom, dateTo, of)
                        .map(m -> modelMapper.map(m, BooksFDto.class));
            } catch (ParseException | NullPointerException e) {
                return repository.findBooks(userId, q.getSearch(), q.isBestseller(), q.isDiscount(), new Date(userId), new Date(), of)
                        .map(m -> modelMapper.map(m, BooksFDto.class));
            }
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
}
