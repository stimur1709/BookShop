package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.model.user.UserLoginHistory;
import com.example.mybookshopapp.repository.UserLoginHistoryRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class UserLoginHistoryService {

    private final UserLoginHistoryRepository userLoginHistoryRepository;
    private final UserProfileService userProfileService;
    private final HttpServletRequest request;

    @Autowired
    public UserLoginHistoryService(UserLoginHistoryRepository userLoginHistoryRepository,
                                   UserProfileService userProfileService, HttpServletRequest request) {
        this.userLoginHistoryRepository = userLoginHistoryRepository;
        this.userProfileService = userProfileService;
        this.request = request;
    }

    @Transactional
    public void saveLoginHistory() {
        User user = userProfileService.getCurrentUser();
        UserLoginHistory userLoginHistory = new UserLoginHistory();
        userLoginHistory.setUser(user);
        userLoginHistory.setDate(new Date());
        String osName = System.getProperty("os.name") + " " + request.getHeader("user-agent");
        userLoginHistory.setOsName(osName);
        userLoginHistory.setIpAddress(request.getRemoteAddr());
        userLoginHistoryRepository.save(userLoginHistory);
    }
}
