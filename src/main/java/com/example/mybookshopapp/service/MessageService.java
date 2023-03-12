package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.MessageDto;
import com.example.mybookshopapp.data.entity.book.review.Message;
import com.example.mybookshopapp.repository.MessageRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserProfileService userProfileService;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserProfileService userProfileService) {
        this.messageRepository = messageRepository;
        this.userProfileService = userProfileService;
    }

    public void sendMessage(MessageDto messageDto) {
        messageRepository.save(new Message(userProfileService.getUserId(), messageDto.getMail(), messageDto.getName(), messageDto.getTitle(), messageDto.getText()));
    }
}
