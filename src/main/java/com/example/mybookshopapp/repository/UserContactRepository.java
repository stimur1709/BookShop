package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.user.User;
import com.example.mybookshopapp.data.entity.user.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserContactRepository extends JpaRepository<UserContact, Integer> {

    Optional<UserContact> findByContactIgnoreCase(String contact);

    List<UserContact> findByUserAndApproved(User user, short approved);

    @Transactional
    long deleteByContact(String contact);

    UserContact findFirstByApproved(short approved);

}
