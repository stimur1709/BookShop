package com.example.mybookshopapp.security.token;

import com.example.mybookshopapp.security.BookstoreUserDetails;
import com.example.mybookshopapp.service.BlacklistService;
import com.example.mybookshopapp.service.BookStoreUserDetailsService;
import com.example.mybookshopapp.service.UserLoginHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JWTRequestFilter extends OncePerRequestFilter {

    private final BookStoreUserDetailsService bookStoreUserDetailsService;
    private final JWTUtil jwtUtil;
    private final BlacklistService blacklistService;
    private final UserLoginHistoryService userLoginHistoryService;

    @Autowired
    public JWTRequestFilter(BookStoreUserDetailsService bookStoreUserDetailsService, JWTUtil jwtUtil,
                            BlacklistService blacklistService, UserLoginHistoryService userLoginHistoryService) {
        this.bookStoreUserDetailsService = bookStoreUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.blacklistService = blacklistService;
        this.userLoginHistoryService = userLoginHistoryService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token;
        String username;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    try {
                        token = cookie.getValue();
                        username = jwtUtil.extractUsername(token);
                        BookstoreUserDetails userDetails =
                                (BookstoreUserDetails) bookStoreUserDetailsService.loadUserByUsername(username);
                        if (username != null && jwtUtil.validateToken(token, userDetails) && blacklistService.findToken(username)) {
                            UsernamePasswordAuthenticationToken authenticationToken =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                                log.info("Аутентификация");
                                userLoginHistoryService.saveLoginHistory(userDetails.getUser(), request);
                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            }
                        }
                    } catch (Exception ex) {
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
