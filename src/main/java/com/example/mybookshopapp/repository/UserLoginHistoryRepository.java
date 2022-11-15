package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.user.UserLoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginHistoryRepository extends JpaRepository<UserLoginHistory, Integer> {
}
