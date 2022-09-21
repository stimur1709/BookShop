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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.equals("anonymousUser"))
            return new UserDto("anonymousUser");

        BookstoreUserDetails bookstoreUserDetails =
                (BookstoreUserDetails) principal;


        User user = bookstoreUserDetails.getUserContact().getUser();
        String mail = "";
        String phone = "";

        for (UserContact contact : userRepository.getById(user.getId()).getUserContact()) {
            if (contact.getType() == ContactType.MAIL)
                mail = contact.getContact();

            if (contact.getType() == ContactType.PHONE)
                phone = contact.getContact();
        }

        return new UserDto(user.getId(), user.getName(), mail, phone, user.getBalance());
    }

    public boolean isAuthenticatedUser() {
        return !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser");
    }
}
