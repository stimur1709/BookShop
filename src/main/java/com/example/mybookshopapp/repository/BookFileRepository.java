package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.book.file.BookFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookFileRepository extends JpaRepository<BookFile, Integer> {

    public BookFile findBookFileByHash(String hash);
}
