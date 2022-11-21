package com.example.mybookshopapp.security;

import com.example.mybookshopapp.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    private final BlacklistService blacklistService;

    @Autowired
    public CustomLogoutSuccessHandler(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("token")).forEach(blacklistService::add);
        response.sendRedirect(request.getHeader("Referer"));

        super.onLogoutSuccess(request, response, authentication);
    }
}
