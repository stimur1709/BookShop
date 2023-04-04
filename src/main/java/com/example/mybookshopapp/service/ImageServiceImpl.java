package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.ImageDto;
import com.example.mybookshopapp.data.entity.Image;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.repository.ImageRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageServiceImpl extends ModelServiceImpl<Image, Query, ImageDto, ImageRepository> {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    protected ImageServiceImpl(ImageRepository repository, UserProfileService userProfileService,
                               ModelMapper modelMapper, HttpServletRequest request) {
        super(repository, ImageDto.class, Image.class, userProfileService, modelMapper, request);
    }

    public List<ImageDto> saveImage(MultipartFile[] files) throws IOException {
        if (!new File(uploadPath).exists()) {
            Files.createDirectories(Paths.get(uploadPath));
        }
        List<ImageDto> images = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.getContentType() != null) {
                String fileName = "/img/" + UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
                Path path = Paths.get(uploadPath, fileName);
                images.add(new ImageDto(fileName, file.getSize()));
                file.transferTo(path);
            }
        }
        return saveAll(images);
    }


}