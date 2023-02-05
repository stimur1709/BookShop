package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.config.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email, Integer> {

    Email findFirstByIsMain(boolean isMain);

}
