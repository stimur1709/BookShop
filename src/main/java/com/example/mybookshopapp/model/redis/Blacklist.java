package com.example.mybookshopapp.model.redis;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Data
@RedisHash("blacklist")
public class Blacklist {

    @Id
    private String id;

    public String token;

    public Blacklist(String id, String token) {
        this.id = id;
        this.token = token;
    }
}