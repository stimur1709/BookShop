package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserContactRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import com.example.mybookshopapp.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserContactService {

    private final UserContactRepository userContactRepository;
    private final Generator generator;
    private final PasswordEncoder passwordEncoder;
    private final UserProfileService userProfileService;

    @Autowired
    public UserContactService(UserContactRepository userContactRepository, Generator generator, PasswordEncoder passwordEncoder,
                              UserProfileService userProfileService) {
        this.userContactRepository = userContactRepository;
        this.generator = generator;
        this.passwordEncoder = passwordEncoder;
        this.userProfileService = userProfileService;
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
        userContact.setCode(passwordEncoder.encode(generator.getSecretCode()));
        userContact.setCodeTime(new Date());
        userContact.setApproved((short) 0);
        userContact.setCodeTrails(0);
        save(userContact);
    }

    public UserContact changeContact(UserContact userNewContact, User user) {
        userNewContact.setUser(user);
        userNewContact.setCode(passwordEncoder.encode(generator.getSecretCode()));
        userNewContact.setCodeTime(new Date());
        userNewContact.setApproved((short) 0);
        userNewContact.setCodeTrails(0);
        save(userNewContact);
        return userNewContact;
    }

    public void delete(UserContact userContact) {
        userContactRepository.delete(userContact);
    }

    @Transactional
    public void deleteAllNoApprovedUserContactByUser() {
        User user = userProfileService.getCurrentUser();
        List<UserContact> userContacts = userContactRepository.findByUserAndApproved(user, (short) 0);
        userContactRepository.deleteAll(userContacts);
        userProfileService.getCurrentUserDTO();
    }
}
