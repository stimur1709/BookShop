package com.example.mybookshopapp.service;

import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DownloadService {

    private final FileDownloadRepository fileDownloadRepository;
    private final UserProfileService userProfileService;

    @Autowired
    public DownloadService(FileDownloadRepository fileDownloadRepository,
                           UserProfileService userProfileService) {
        this.fileDownloadRepository = fileDownloadRepository;
        this.userProfileService = userProfileService;
    }

    public void fileDownload(String hash) {
        fileDownloadRepository.fileDownload(hash, userProfileService.getUserId());
    }
}
