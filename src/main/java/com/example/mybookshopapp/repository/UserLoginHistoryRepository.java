package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.model.user.UserLoginHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoginHistoryRepository extends JpaRepository<UserLoginHistory, Integer> {

    Page<UserLoginHistory> findByUser(User user, Pageable pageable);

    Optional<UserLoginHistory> findFirstBySystemAndIpAddressAndUserOrderByDateAsc(String system, String ipAddress, User user);

}
