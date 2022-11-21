package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.model.user.UserLoginHistory;
import com.example.mybookshopapp.repository.UserLoginHistoryRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class UserLoginHistoryService {

    private final UserLoginHistoryRepository userLoginHistoryRepository;
    private final UserProfileService userProfileService;
    private final HttpServletRequest request;
    private final LocaleResolver localeResolver;

    @Autowired
    public UserLoginHistoryService(UserLoginHistoryRepository userLoginHistoryRepository,
                                   UserProfileService userProfileService, HttpServletRequest request,
                                   LocaleResolver localeResolver) {
        this.userLoginHistoryRepository = userLoginHistoryRepository;
        this.userProfileService = userProfileService;
        this.request = request;
        this.localeResolver = localeResolver;
    }

    @Transactional
    public void saveLoginHistory() {
        User user = userProfileService.getCurrentUser();
        String system = System.getProperty("os.name") + " " + request.getHeader("user-agent");
        String ipAddress = request.getRemoteAddr();
        Optional<UserLoginHistory> userLoginHistory = userLoginHistoryRepository.findFirstBySystemAndIpAddressAndUserOrderByDateAsc(system, ipAddress, user);
        if (userLoginHistory.isPresent()) {
            if (Math.abs(userLoginHistory.get().getDate().getTime() - new Date().getTime()) > 300000) {
                userLoginHistory.get().setDate(new Date());
                userLoginHistoryRepository.save(userLoginHistory.get());
            }
        } else {
            userLoginHistoryRepository.save(new UserLoginHistory(system, ipAddress, user));
        }
    }

    public Page<UserLoginHistory> getPageLoginHistory(int page, int size) {
        User user = userProfileService.getCurrentUser();
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<UserLoginHistory> result = userLoginHistoryRepository.findByUser(user, pageable);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm", localeResolver.resolveLocale(request));
        result.getContent().forEach(userLoginHistory -> userLoginHistory.setFormatDate(simpleDateFormat));
        return result;
    }
}
