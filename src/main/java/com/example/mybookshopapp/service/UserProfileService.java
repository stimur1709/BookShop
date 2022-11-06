package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dto.UserDto;
import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.model.user.UserContact;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class UserProfileService {

    private final UserContactService userContactService;

    public UserProfileService(UserContactService userContactService) {
        this.userContactService = userContactService;
    }

    public UserDto getCurrentUserDTO() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!username.equals("anonymousUser")) {
            User user = userContactService.getUserContact(username).getUser();
            String mail = "";
            String phone = "";

            for (UserContact contact : user.getUserContact()) {
                if (contact.getType() == ContactType.MAIL)
                    mail = contact.getContact();

                if (contact.getType() == ContactType.PHONE)
                    phone = contact.getContact();
            }

            return new UserDto(user.getId(), user.getLastname(), mail, phone, user.getBalance());
        }
        return null;
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userContactService.getUserContact(username).getUser();
    }

    public boolean isAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return !username.equals("anonymousUser");
    }
}
