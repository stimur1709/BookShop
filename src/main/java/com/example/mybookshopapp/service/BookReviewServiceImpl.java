package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.BookReviewDto;
import com.example.mybookshopapp.data.entity.books.BookReview;
import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.errors.DefaultException;
import com.example.mybookshopapp.repository.BookReviewQueryRepository;
import com.example.mybookshopapp.repository.BookReviewRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import com.example.mybookshopapp.util.MessageLocale;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookReviewServiceImpl extends ModelServiceImpl<BookReview, Query, BookReviewDto, BookReviewDto, BookReviewRepository> {

    private final BookReviewQueryRepository bookReviewQueryRepository;
    private final MessageLocale messageLocale;

    protected BookReviewServiceImpl(BookReviewRepository repository, UserProfileService userProfileService,
                                    ModelMapper modelMapper, HttpServletRequest request,
                                    BookReviewQueryRepository bookReviewQueryRepository, MessageLocale messageLocale) {
        super(repository, BookReviewDto.class, BookReviewDto.class, BookReview.class, userProfileService, modelMapper, request);
        this.bookReviewQueryRepository = bookReviewQueryRepository;
        this.messageLocale = messageLocale;
    }

    @Override
    public BookReviewDto save(BookReviewDto dto) throws DefaultException {
        if (!userProfileService.isAuthenticatedUser()) {
            throw new DefaultException(messageLocale.getMessage("message.onlyAuth"));
        }
        User user = userProfileService.getCurrentUser();
        dto.setUserId(user.getId());
        BookReviewDto reviewDto = super.save(dto);
        reviewDto.setName(user.getFirstname() + ' ' + user.getLastname());
        reviewDto.setDate(messageLocale.getLocaleDate(new Date()));
        return reviewDto;
    }

    @Override
    public List<BookReviewDto> getListContents(Query q) {
        User user = userProfileService.getCurrentUser();
        return bookReviewQueryRepository.getReviewByBookAndUser(q.getSearch(), user.getId())
                .stream()
                .map(m -> modelMapper.map(m, BookReviewDto.class))
                .collect(Collectors.toList());
    }

}
