package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.redis.Blacklist;
import org.springframework.data.repository.CrudRepository;

public interface BlacklistRepository extends CrudRepository<Blacklist, String> {
}
