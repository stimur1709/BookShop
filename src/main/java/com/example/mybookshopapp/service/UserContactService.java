package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserContactRepository;
import com.example.mybookshopapp.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserContactService {

    private final UserContactRepository userContactRepository;
    private final Generator generator;

    @Autowired
    public UserContactService(UserContactRepository userContactRepository, Generator generator) {
        this.userContactRepository = userContactRepository;
        this.generator = generator;
    }

    public Optional<UserContact> checkUserExistsByContact(String contact) {
        return userContactRepository.findByContactIgnoreCase(contact);
    }

    public UserContact getUserContact(String contact) {
        return userContactRepository.findByContactIgnoreCase(contact).orElse(null);
    }

    public UserContact save(UserContact userContact) {
        return userContactRepository.save(userContact);
    }

    public void changeContact(UserContact userContact) {
        userContact.setCode(generator.getSecretCode());
        userContact.setCodeTime(new Date());
        userContact.setApproved((short) 0);
        userContact.setCodeTrails(0);
        save(userContact);
    }

    public void delete(UserContact userContact) {
        userContactRepository.delete(userContact);
    }
}
