package com.example.mybookshopapp.security.token;

import com.example.mybookshopapp.security.BookstoreUserDetails;
import com.example.mybookshopapp.service.BlacklistService;
import com.example.mybookshopapp.service.BookStoreUserDetailsService;
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
public class JWTRequestFilter extends OncePerRequestFilter {

    private final BookStoreUserDetailsService bookStoreUserDetailsService;
    private final JWTUtil jwtUtil;
    private final BlacklistService blacklistService;

    @Autowired
    public JWTRequestFilter(BookStoreUserDetailsService bookStoreUserDetailsService, JWTUtil jwtUtil,
                            BlacklistService blacklistService) {
        this.bookStoreUserDetailsService = bookStoreUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.blacklistService = blacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String username = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                try {
                    if (cookie.getName().equals("token")) {
                        token = cookie.getValue();
                        username = jwtUtil.extractUsername(token);
                    }
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        BookstoreUserDetails userDetails =
                                (BookstoreUserDetails) bookStoreUserDetailsService.loadUserByUsername(username);
                        if (jwtUtil.validateToken(token, userDetails) && blacklistService.findToken(username)) {
                            UsernamePasswordAuthenticationToken authenticationToken =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails, null, userDetails.getAuthorities()
                                    );
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    }
                } catch (Exception ex) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    System.out.println("Токен удален");
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
