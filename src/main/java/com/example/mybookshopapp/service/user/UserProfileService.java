package com.example.mybookshopapp.service.user;

import com.example.mybookshopapp.data.dto.UserDto;
import com.example.mybookshopapp.data.entity.enums.ContactType;
import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.data.entity.user.UserContact;
import com.example.mybookshopapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserNotAuthService userNotAuthService;

    @Autowired
    public UserProfileService(UserRepository userRepository, UserNotAuthService userNotAuthService) {
        this.userRepository = userRepository;
        this.userNotAuthService = userNotAuthService;
    }

    @Transactional
    public UserDto getCurrentUserDTO() {
        String hash = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!hash.equals("anonymousUser")) {
            Optional<User> userOptional = userRepository.findByHash(hash);
            if (userOptional.isPresent()) {
                return mapperDto(userOptional.get());
            }
        }
        return null;
    }

    private UserDto mapperDto(User user) {
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
        return new UserDto(user.getFirstname(), user.getLastname(), mail, approvedMail, phone, approvedPhone, user.getBalance());
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAuthenticatedUser()) {
            String hash = authentication.getName();
            return userRepository.findByHash(hash).orElse(null);
        }
        return userNotAuthService.createNotAuthUser();
    }

    public Integer getUserId() {
        User user = getCurrentUser();
        return user == null ? -1 : user.getId();
    }

    public boolean isAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return !authentication.getName().equals("anonymousUser");
    }
}
