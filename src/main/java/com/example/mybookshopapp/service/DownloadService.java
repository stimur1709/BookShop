package com.example.mybookshopapp.service;

import com.example.mybookshopapp.repository.FileDownloadRepository;
import com.example.mybookshopapp.service.user.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class DownloadService {

    private final FileDownloadRepository fileDownloadRepository;
    private final UserProfileService userProfileService;
    private final BookFileServiceImpl bookFileService;

    @Autowired
    public DownloadService(FileDownloadRepository fileDownloadRepository,
                           UserProfileService userProfileService, BookFileServiceImpl bookFileService) {
        this.fileDownloadRepository = fileDownloadRepository;
        this.userProfileService = userProfileService;
        this.bookFileService = bookFileService;
    }

    public ResponseEntity<ByteArrayResource> fileDownload(String hash) throws IOException {
        fileDownloadRepository.fileDownload(hash, userProfileService.getUserId());
        Path path = bookFileService.getBookFilePath(hash);
        MediaType mediaType = bookFileService.getBookFileName(hash);
        byte[] data = bookFileService.getBookFileByteArray(hash);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }
}
