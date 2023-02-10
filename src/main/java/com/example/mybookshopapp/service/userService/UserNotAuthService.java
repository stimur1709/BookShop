package com.example.mybookshopapp.service.userService;

import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
public class UserNotAuthService {

    private final UserRepository userRepository;
    private final HttpServletResponse response;

    @Autowired
    public UserNotAuthService(UserRepository userRepository, HttpServletResponse response) {
        this.userRepository = userRepository;
        this.response = response;
    }

    public User createNotAuthUser() {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<User> user = userRepository.findByHash(sessionId);
        if (user.isPresent()) {
            return user.get();
        }
        response.addCookie(new Cookie("session", sessionId));
        return userRepository.save(new User("Anonim", "Anonimus", sessionId));
    }


}
