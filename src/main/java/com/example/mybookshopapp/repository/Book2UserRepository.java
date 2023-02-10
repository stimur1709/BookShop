package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.book.links.Book2User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface Book2UserRepository extends JpaRepository<Book2User, Integer> {

    @Modifying
    @Transactional
    @Query(value = "insert into book2user(book_id, user_id, type_id) " +
            "values (?1, ?2, ?3) " +
            "on conflict(book_id, user_id) do update set book_id = ?1, " +
            "                                                     user_id = ?2, " +
            "                                                     type_id = ?3", nativeQuery = true)
    void updateOrCreateType(int bookId, int userId, int typeId);

    @Modifying
    @Transactional
    @Query(value = "update book2user set user_id = ?2 where user_id = ?1 and type_id not in (3, 4)", nativeQuery = true)
    void updateUserBooksType(int userOld, int userNew);
}
