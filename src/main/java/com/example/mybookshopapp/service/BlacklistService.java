package com.example.mybookshopapp.service;

import com.example.mybookshopapp.repository.BlacklistRepository;
import com.example.mybookshopapp.security.token.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
public class BlacklistService {

    private final BlacklistRepository blacklistRepository;
    private final JWTUtil jwtUtil;

    @Autowired
    public BlacklistService(BlacklistRepository blacklistRepository, JWTUtil jwtUtil) {
        this.blacklistRepository = blacklistRepository;
        this.jwtUtil = jwtUtil;
    }

    public void add(Cookie cookie) {
        String key = jwtUtil.extractUsername(cookie.getValue());
        long exp = jwtUtil.extractExpiration(cookie.getValue()).getTime();
        blacklistRepository.add(key, exp);
    }

    public boolean findToken(String key) {
        return blacklistRepository.findToken(key).isEmpty();
    }

    public void delete(String token) {
        blacklistRepository.delete(jwtUtil.extractUsername(token));
    }
}
