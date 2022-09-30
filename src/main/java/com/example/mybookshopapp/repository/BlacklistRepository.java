package com.example.mybookshopapp.repository;

import java.util.Set;

public interface BlacklistRepository {

    Set<String> findToken(String key);

    void add(String key, long time);

    void delete(String userContact);
}
