package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dto.UserDto;
import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserRepository;
import com.example.mybookshopapp.security.BookstoreUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserRepository userRepository;


    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getCurrentUser() {
        BookstoreUserDetails bookstoreUserDetails =
                (BookstoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = bookstoreUserDetails.getUserContact().getUser();
        String mail = "";
        String phone = "";

        for (UserContact contact : userRepository.getById(user.getId()).getUserContact()) {
            if (contact.getType() == ContactType.EMAIL)
                mail = contact.getContact();

            if (contact.getType() == ContactType.PHONE)
                phone = contact.getContact();
        }

        return new UserDto(user.getName(), mail, phone);
    }
}
