package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.BookStatusRequestDto;
import com.example.mybookshopapp.data.dto.ResponseResultDto;
import com.example.mybookshopapp.data.entity.book.Book;
import com.example.mybookshopapp.data.entity.book.links.Book2User;
import com.example.mybookshopapp.data.entity.book.links.Book2UserType;
import com.example.mybookshopapp.data.entity.book.links.BookCodeType;
import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.repository.Book2UserRepository;
import com.example.mybookshopapp.repository.Book2UserTypeRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Book2UserTypeService {

    private final BookService bookService;
    private final BooksRatingAndPopularityService booksRatingAndPopularityService;
    private final UserProfileService userProfileService;
    private final Book2UserTypeRepository book2UserTypeRepository;
    private final Book2UserRepository book2UserRepository;
    private final CookieBooksService cookieBooksService;

    @Autowired
    public Book2UserTypeService(BookService bookService,
                                BooksRatingAndPopularityService booksRatingAndPopularityService,
                                UserProfileService userProfileService, Book2UserTypeRepository book2UserTypeRepository,
                                Book2UserRepository book2UserRepository, CookieBooksService cookieBooksService) {
        this.bookService = bookService;
        this.booksRatingAndPopularityService = booksRatingAndPopularityService;
        this.userProfileService = userProfileService;
        this.book2UserTypeRepository = book2UserTypeRepository;
        this.book2UserRepository = book2UserRepository;
        this.cookieBooksService = cookieBooksService;
    }

    public ResponseResultDto changeBookStatus(BookStatusRequestDto dto) {
        String[] slugs = dto.getBooksIds().replace("[", "").replace("]", "").split(", ");
        for (String slug : slugs) {
            Book book = bookService.getBookBySlug(slug);
            User user = userProfileService.getCurrentUser();
            switch (dto.getStatus()) {
                case CART: {
                    changeTypeBook2User(book, user, BookCodeType.CART, true);
                    booksRatingAndPopularityService.changePopularity(book, 0.7);
                    break;
                }
                case KEPT: {
                    changeTypeBook2User(book, user, BookCodeType.KEPT, true);
                    booksRatingAndPopularityService.changePopularity(book, 0.4);
                    break;
                }
                case UNLINK: {
                    changeTypeBook2User(book, user, BookCodeType.UNLINK, true);
                    break;
                }
                default:
                    break;
            }
        }
        return new ResponseResultDto(true);
    }

    private void changeTypeBook2User(Book book, User user, BookCodeType status, boolean rating) {
        Optional<Book2User> optionalBook2User = book2UserRepository.findByUserAndBook(user, book);
        Book2User book2User;
        Book2UserType bookCodeType = book2UserTypeRepository.findByCode(status);
        if (optionalBook2User.isEmpty()) {
            book2User = new Book2User(bookCodeType, book, user);
            book2UserRepository.save(book2User);
        } else {
            book2User = optionalBook2User.get();
            if (rating) {
                booksRatingAndPopularityService.changePopularity(book, getValue(book2User.getType().getCode()));
            }
            book2User.setType(bookCodeType);
            book2UserRepository.save(book2User);
        }
    }

    public List<Book> getBooksUser(BookCodeType status) {
        List<Book2User> book2Users =
                book2UserRepository.findByType_CodeAndUser_Id(status, userProfileService.getCurrentUser().getId());
        return !book2Users.isEmpty()
                ? book2Users.stream().map(Book2User::getBook).collect(Collectors.toList()) : Collections.emptyList();
    }

    public BookCodeType getBookStatus(Book book) {
        User user = userProfileService.getCurrentUser();
        return book2UserRepository.findByUserAndBook(user, book).map(value -> value.getType().getCode()).orElse(BookCodeType.UNLINK);
    }

    private double getValue(BookCodeType status) {
        return status.equals(BookCodeType.CART) ? -0.7 : -0.4;
    }

    public void addBooksTypeUserFromCookie(User user) {
        Map<BookCodeType, List<Book>> books = cookieBooksService.getBooksFromCookies();
        if (!books.isEmpty()) {
            books.forEach((key, value) -> value.forEach(book -> changeTypeBook2User(book, user, key, false)));
        }
    }
}