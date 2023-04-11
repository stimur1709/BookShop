package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.MessageDto;
import com.example.mybookshopapp.data.entity.Message;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.errors.DefaultException;
import com.example.mybookshopapp.repository.MessageRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class MessageService extends ModelServiceImpl<Message, Query, MessageDto, MessageDto, MessageRepository> {

    private final UserProfileService userProfileService;

    @Autowired
    protected MessageService(MessageRepository repository, UserProfileService userProfileService,
                             ModelMapper modelMapper, HttpServletRequest request, UserProfileService userProfileService1) {
        super(repository, MessageDto.class, MessageDto.class, Message.class, userProfileService, modelMapper, request);
        this.userProfileService = userProfileService1;
    }

    @Override
    public MessageDto save(MessageDto dto) throws DefaultException {
        dto.setUserId(userProfileService.getUserId());
        return super.save(dto);
    }

}
