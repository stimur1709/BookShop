package com.example.mybookshopapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Set;

@Repository
public class BlacklistRepositoryImpl implements BlacklistRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, Object, Object> hashOperations;

    @Autowired
    public BlacklistRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public Set<String> findToken(String key) {
        return redisTemplate.keys(key);
    }

    @Override
    public void add(String key, long time) {
        hashOperations.put(key, key, time);
        redisTemplate.expire(key, Duration.ofMillis(time));
    }

    @Override
    public void delete(String userContact) {
        redisTemplate.delete(userContact);
    }
}
