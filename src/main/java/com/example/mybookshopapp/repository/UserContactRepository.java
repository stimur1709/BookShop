package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.user.UserContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserContactRepository extends JpaRepository<UserContactEntity, Integer> {

    Optional<UserContactEntity> findUserContactEntityByContact(String email);
}
