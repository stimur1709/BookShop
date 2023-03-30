package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.books.BookFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookFileRepository extends JpaRepository<BookFile, Integer> {

    BookFile findBookFileByHash(String hash);
}
