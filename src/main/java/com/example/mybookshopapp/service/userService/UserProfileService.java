package com.example.mybookshopapp.service.userService;

import com.example.mybookshopapp.dto.UserDto;
import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.model.user.UserContact;
import com.example.mybookshopapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class UserProfileService {

    private final UserRepository userRepository;

    @Autowired
    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDto getCurrentUserDTO() {
        String hash = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!hash.equals("anonymousUser")) {
            Optional<User> userOptional = userRepository.findByHash(hash);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String mail = "";
                int approvedMail = 0;
                String phone = "";
                int approvedPhone = 0;

                for (UserContact contact : user.getUserContact()) {
                    if (contact.getType() == ContactType.MAIL) {
                        mail = contact.getContact();
                        approvedMail = contact.getApproved();
                    }

                    if (contact.getType() == ContactType.PHONE) {
                        phone = contact.getContact();
                        approvedPhone = contact.getApproved();
                    }
                }
                return new UserDto(user.getId(), user.getFirstname(), user.getLastname(), mail, approvedMail, phone, approvedPhone, user.getBalance());
            }
        }
        return null;
    }

    public User getCurrentUser() {
        String hash = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByHash(hash).orElse(null);
    }

    public boolean isAuthenticatedUser() {
        String hash = SecurityContextHolder.getContext().getAuthentication().getName();
        return !hash.equals("anonymousUser");
    }
}
