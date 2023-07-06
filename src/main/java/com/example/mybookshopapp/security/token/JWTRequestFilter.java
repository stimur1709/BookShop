package com.example.mybookshopapp.security.token;

import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.data.entity.user.UserRole;
import com.example.mybookshopapp.errors.AdminException;
import com.example.mybookshopapp.security.BookstoreUserDetails;
import com.example.mybookshopapp.service.BlacklistService;
import com.example.mybookshopapp.service.BookStoreUserDetailsService;
import com.example.mybookshopapp.service.UserLoginHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Optional;

@Component
@Slf4j
public class JWTRequestFilter extends OncePerRequestFilter {

    private final BookStoreUserDetailsService bookStoreUserDetailsService;
    private final JWTUtil jwtUtil;
    private final BlacklistService blacklistService;
    private final UserLoginHistoryService userLoginHistoryService;

    @Value("${cms.url}")
    private String crmUrl;

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
        Cookie cookie = getCookie(request);
        if (cookie != null) {
            try {
                authUser(cookie.getValue(), request, false);
            } catch (Exception ex) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        } else if (request.getHeader("Referer") != null && request.getHeader("Referer").contains(crmUrl)) {
            String token = getHeader(request);
            if (token != null) {
                try {
                    authUser(token, request, true);
                } catch (Exception ex) {
                    response.setStatus(403);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void authUser(String jwt, HttpServletRequest request, boolean admin) throws AdminException {
        String username = jwtUtil.extractUsername(jwt);
        BookstoreUserDetails userDetails =
                (BookstoreUserDetails) bookStoreUserDetailsService.loadUserByUsername(username);
        if (username != null && jwtUtil.validateToken(jwt, userDetails) && blacklistService.findToken(username)) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            checkAdminUser(admin, userDetails.getUser());
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("Аутентификация");
                userLoginHistoryService.saveLoginHistory(userDetails.getUser(), request);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    }

    private Cookie getCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    return cookie;
                }
            }
        }
        return null;
    }

    private String getHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private void checkAdminUser(boolean admin, User user) throws AdminException {
        if (admin) {
            Optional<String> first = user.getUserRoles()
                    .stream()
                    .map(UserRole::getRole)
                    .filter(r -> r.contains("ADMIN"))
                    .findFirst();
            if (first.isEmpty()) {
                throw new AdminException();
            }
        }
    }
}
