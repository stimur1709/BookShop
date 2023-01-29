package com.example.mybookshopapp.service;

import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.model.user.UserLoginHistory;
import com.example.mybookshopapp.repository.UserLoginHistoryRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import com.example.mybookshopapp.util.UserGeolocation;
import io.ipgeolocation.api.Geolocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
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
    private final LocaleResolver localeResolver;
    private final UserGeolocation userGeolocation;
    private final HttpServletRequest request;

    @Autowired
    public UserLoginHistoryService(UserLoginHistoryRepository userLoginHistoryRepository,
                                   UserProfileService userProfileService,
                                   LocaleResolver localeResolver, UserGeolocation userGeolocation, HttpServletRequest request) {
        this.userLoginHistoryRepository = userLoginHistoryRepository;
        this.userProfileService = userProfileService;
        this.localeResolver = localeResolver;
        this.userGeolocation = userGeolocation;
        this.request = request;
    }

    @Async
    @Transactional
    public void saveLoginHistory(User user, HttpServletRequest request) {
        String system = System.getProperty("os.name");
        String agent = request.getHeader("user-agent");
        String ipAddress = request.getRemoteAddr();
        if (ipAddress != null && agent != null) {
            Optional<UserLoginHistory> userLoginHistory = userLoginHistoryRepository.findFirstBySystemAndIpAddressAndUserOrderByDateAsc(system + ' ' + agent, ipAddress, user);
            if (userLoginHistory.isPresent()) {
                if (Math.abs(userLoginHistory.get().getDate().getTime() - new Date().getTime()) > 300000) {
                    userLoginHistory.get().setDate(new Date());
                    userLoginHistoryRepository.saveAndFlush(userLoginHistory.get());
                }
            } else {
                Geolocation geolocation = userGeolocation.getGeolocation();
                userLoginHistoryRepository.saveAndFlush(
                        new UserLoginHistory(system + ' ' + agent, ipAddress, geolocation.getCity(), geolocation.getCountryName(), user)
                );
            }
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
