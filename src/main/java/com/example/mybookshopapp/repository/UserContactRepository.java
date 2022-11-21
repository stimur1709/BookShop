package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.model.user.User;
import com.example.mybookshopapp.model.user.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserContactRepository extends JpaRepository<UserContact, Integer> {

    Optional<UserContact> findByContactIgnoreCase(String contact);

    List<UserContact> findByUserAndApproved(User user, short approved);

}
