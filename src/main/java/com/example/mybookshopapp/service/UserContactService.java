package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserContactService {

    private final UserContactRepository userContactRepository;

    @Autowired
    public UserContactService(UserContactRepository userContactRepository) {
        this.userContactRepository = userContactRepository;
    }

    public Optional<UserContact> checkUserExistsByContact(String contact) {
        return userContactRepository.findUserContactEntityByContact(contact);
    }

    public UserContact getUserContact(String contact) {
        return userContactRepository.findUserContactEntityByContact(contact).orElse(null);
    }
}
