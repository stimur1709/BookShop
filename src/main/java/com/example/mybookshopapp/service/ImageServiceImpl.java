package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.ImageDto;
import com.example.mybookshopapp.data.entity.Image;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.repository.ImageRepository;
import com.example.mybookshopapp.service.user.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImageServiceImpl extends ModelServiceImpl<Image, Query, ImageDto, ImageDto, ImageRepository> {

    @Value("${upload.path}")
    private String uploadPath;

    private final RestTemplate restTemplate;

    @Autowired
    protected ImageServiceImpl(ImageRepository repository, UserProfileService userProfileService,
                               ModelMapper modelMapper, HttpServletRequest request, RestTemplate restTemplate) {
        super(repository, ImageDto.class, ImageDto.class, Image.class, userProfileService, modelMapper, request);
        this.restTemplate = restTemplate;
    }

    public List<ImageDto> saveImage(MultipartFile[] files) throws IOException {
        if (!new File(uploadPath).exists()) {
            Files.createDirectories(Paths.get(uploadPath));
        }
        List<ImageDto> images = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.getContentType() != null) {
                String fileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
                Path path = Paths.get(uploadPath, fileName);
                images.add(new ImageDto(fileName, file.getSize()));
                file.transferTo(path);
            }
        }
        return saveAll(images);
    }

    public Image downloadImage(String url) {
        String fileName = UUID.randomUUID() + ".jpg";
        try (FileOutputStream fos = new FileOutputStream(uploadPath + '/' + fileName)) {
            byte[] imageBytes = restTemplate.getForObject(url, byte[].class);
            if (imageBytes == null) {
                log.error("Картина не нашлась");
                return new Image(1);
            }
            fos.write(imageBytes);
            return new Image(fileName, fos.getChannel().size());
        } catch (IOException | IllegalArgumentException e) {
            log.error("Картина не нашлась");
            return new Image(1);
        }
    }

    @Scheduled(fixedDelayString = "PT24H")
    protected void deletingUnusedImage() {
        log.info("The procedure for deleting unused images has been started");
        int size = deletingUnusedInBd();
        size += deletingUnusedInProject();
        log.info("{} pictures removed", size);
    }

    private int deletingUnusedInBd() {
        List<Image> unusedPictures = repository.findUnusedPictures();
        for (Image image : unusedPictures) {
            File file = new File(uploadPath + File.separator + image.getName());
            delete(file);
        }
        if (!unusedPictures.isEmpty()) {
            repository.deleteAll(unusedPictures);
        }
        return unusedPictures.size();

    }

    private int deletingUnusedInProject() {
        int size = 0;
        File dir = new File(uploadPath);
        File[] arrFiles = dir.listFiles();
        if (arrFiles != null && arrFiles.length > 0) {
            List<String> imagesName = Arrays.stream(arrFiles).map(File::getName).collect(Collectors.toList());
            List<String> imagesNameBD = repository.findByNameIn(imagesName)
                    .stream()
                    .map(Image::getName)
                    .collect(Collectors.toList());
            imagesName.removeAll(imagesNameBD);
            List<File> filesDelete = Arrays.stream(arrFiles)
                    .filter(f -> imagesName.contains(f.getName()))
                    .collect(Collectors.toList());
            if (!filesDelete.isEmpty()) {
                for (File file : filesDelete) {
                    delete(file);
                }
                size += filesDelete.size();
            }
        }
        return size;
    }

    public void delete(File file) {
        try {
            Files.delete(file.toPath());
            log.info("{} picture deleted", file.getName());
        } catch (IOException e) {
            log.error("{} image deletion error", file.getName());
            throw new RuntimeException(e);
        }
    }


}