package com.example.mybookshopapp.service.userService;

import com.example.mybookshopapp.repository.UserRepository;
import com.example.mybookshopapp.service.Book2UserTypeService;
import com.example.mybookshopapp.service.UserContactService;
import com.example.mybookshopapp.util.Generator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    protected final UserRepository userRepository;
    protected final PasswordEncoder passwordEncoder;
    protected final Book2UserTypeService book2UserTypeService;
    protected final UserContactService userContactService;
    protected final Generator generator;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       Book2UserTypeService book2UserTypeService, UserContactService userContactService, Generator generator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.book2UserTypeService = book2UserTypeService;
        this.userContactService = userContactService;
        this.generator = generator;
    }
}
