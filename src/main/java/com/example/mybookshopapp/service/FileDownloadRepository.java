package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.entity.book.file.FileDownload;
import com.example.mybookshopapp.data.entity.book.links.key.KeyBook2User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface FileDownloadRepository extends JpaRepository<FileDownload, KeyBook2User> {

    @Async
    @Modifying
    @Transactional
    @Query(value = "insert into file_download(book_id, user_id) " +
            "values ((select book_file.book_id from book_file where hash = ?1), ?2) " +
            "on conflict(book_id, user_id) do update set book_id = (select book_file.book_id from book_file where hash = ?1), " +
            "user_id = ?2, count = file_download.count + 1 ", nativeQuery = true)
    void fileDownload(String hash, int userId);

}