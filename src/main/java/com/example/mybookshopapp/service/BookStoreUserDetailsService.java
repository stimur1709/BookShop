package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.repository.UserRepository;
import com.example.mybookshopapp.security.BookstoreUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookStoreUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public BookStoreUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String hash) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByHash(hash);
        if (user.isPresent())
            return new BookstoreUserDetails(user.get());
        else
            throw new UsernameNotFoundException("Пользователь не найден");
    }
}
