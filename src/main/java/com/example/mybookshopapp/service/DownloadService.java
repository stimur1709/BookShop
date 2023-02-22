package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.entity.book.Book;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DownloadService {

    private final FileDownloadRepository fileDownloadRepository;
    private final BookRepository bookRepository;
    private final UserProfileService userProfileService;

    @Autowired
    public DownloadService(FileDownloadRepository fileDownloadRepository,
                           BookRepository bookRepository, UserProfileService userProfileService) {
        this.fileDownloadRepository = fileDownloadRepository;
        this.bookRepository = bookRepository;
        this.userProfileService = userProfileService;
    }

    public int fileDownload(String hash) {
        Book book = bookRepository.findByBookFileList_Hash(hash);
        return fileDownloadRepository.fileDownload(book.getId(), userProfileService.getUserId());
    }
}
