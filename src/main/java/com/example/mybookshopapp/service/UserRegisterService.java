package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserContactRepository;
import com.example.mybookshopapp.repository.UserRepository;
import com.example.mybookshopapp.security.model.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegisterService {

    private final UserRepository userRepository;
    private final UserContactRepository userContactRepository;
    private final PasswordEncoder passwordEncoder;
    private final Book2UserTypeService book2UserTypeService;

    @Autowired
    public UserRegisterService(UserRepository userRepository,
                               UserContactRepository userContactRepository,
                               PasswordEncoder passwordEncoder,
                               Book2UserTypeService book2UserTypeService) {
        this.userRepository = userRepository;
        this.userContactRepository = userContactRepository;
        this.passwordEncoder = passwordEncoder;
        this.book2UserTypeService = book2UserTypeService;
    }

    public void registerUser(RegistrationForm registrationForm, String cartContent, String keptContent) {
        User user = new User(registrationForm.getName(),
                passwordEncoder.encode(registrationForm.getPassword()));
        UserContact contactEmail = new UserContact(user, ContactType.MAIL, registrationForm.getEmail());
        UserContact contactPhone = new UserContact(user, ContactType.PHONE, registrationForm.getPhone());

        user.getUserContact().add(contactEmail);
        user.getUserContact().add(contactPhone);

        userRepository.save(user);
        userContactRepository.save(contactEmail);
        userContactRepository.save(contactPhone);

        book2UserTypeService.addBooksTypeUserFromCookie(cartContent, keptContent, user);
    }
}