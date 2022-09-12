package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.model.book.links.Book2User;
import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.repository.Book2UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Book2UserService {

    private final Book2UserRepository book2UserRepository;

    @Autowired
    public Book2UserService(Book2UserRepository book2UserRepository) {
        this.book2UserRepository = book2UserRepository;
    }

    public Optional<Book2User> getBook2User(Book book, User user) {
        return book2UserRepository.findByUserIdAndBookId(user.getId(), book.getId());
    }

    public void save(Book2User book2User) {
        book2UserRepository.save(book2User);
    }
}
