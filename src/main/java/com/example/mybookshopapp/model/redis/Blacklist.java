package com.example.mybookshopapp.model.redis;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@RedisHash("Blacklist")
@Data
public class Blacklist {

    private Integer id;

    @Id
    public String token;

    public Blacklist(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Blacklist{" +
                "id=" + id +
                ", token='" + token + '\'' +
                '}';
    }
}
