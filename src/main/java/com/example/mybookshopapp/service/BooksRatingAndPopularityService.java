package com.example.mybookshopapp.service;

import com.example.mybookshopapp.entity.book.links.Book2UserEntity;
import com.example.mybookshopapp.repository.Book2UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BooksRatingAndPopularityService {

    private final Book2UserRepository book2UserRepository;

    @Autowired
    public BooksRatingAndPopularityService(Book2UserRepository book2UserRepository) {
        this.book2UserRepository = book2UserRepository;
    }

    public Map<Integer, Double> getPopularity(Integer bookId) {
        List<Book2UserEntity> bookList = book2UserRepository.findBook2UserEntitiesByBookId(bookId);
        return bookList.stream().collect(Collectors.groupingBy((Book2UserEntity b) -> b.getBook().getId(),
                Collectors.summingDouble(((Book2UserEntity b) -> {
                    if (b.getType().getId() == 1) {
                        return 0.4;
                    }
                    if (b.getType().getId() == 2) {
                        return 0.7;
                    }
                    if (b.getType().getId() == 3) {
                        return 1.0;
                    } else {
                        return 0.0;
                    }
                }))));
    }
}
