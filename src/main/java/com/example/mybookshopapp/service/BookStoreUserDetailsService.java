package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserContactRepository;
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

    private final UserContactRepository userContactRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookStoreUserDetailsService(UserContactRepository userContactRepository, UserRepository userRepository) {
        this.userContactRepository = userContactRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserContact> userContact = userContactRepository.findUserContactEntityByContact(username);
        Optional<User> user = userRepository.findByName(username);

        if (userContact.isPresent())
            return new BookstoreUserDetails(userContact.get().getUser());
        if (user.isPresent())
            return new BookstoreUserDetails(user.get());
        else
            throw new UsernameNotFoundException("Пользователь не найден");
    }
}
