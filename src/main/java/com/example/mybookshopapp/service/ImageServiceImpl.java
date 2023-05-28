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
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    protected ImageServiceImpl(ImageRepository repository, UserProfileService userProfileService,
                               ModelMapper modelMapper, HttpServletRequest request, EntityManager entityManager) {
        super(repository, ImageDto.class, ImageDto.class, Image.class, userProfileService, modelMapper, request);
        this.entityManager = entityManager;
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

    public Image parsingImage(String url) {
        try {
            return dowloadImage(url);
        } catch (IOException | IllegalArgumentException e) {
            log.error("Картина не нашлась");
            return entityManager.merge(new Image(1));
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
            log.error("{} image deletion error, most likely the picture has already been deleted.", file.getName());
        }
    }

    private Image dowloadImage(String url) throws IOException {
        BufferedImage image = ImageIO.read(new URL(url));
        if (image != null) {

            int currentWidth = image.getWidth();
            int currentHeight = image.getHeight();

            int newHeight = Math.min(800, currentHeight);
            int newWidth = currentHeight > newHeight ? (int) (currentWidth * ((double) newHeight / currentHeight)) : currentWidth;

            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, image.getType());
            resizedImage.createGraphics().drawImage(image.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, newWidth, newHeight, null);
            String fileName = UUID.randomUUID() + ".jpg";
            String path = uploadPath + File.separator + fileName;
            File file = new File(path);
            ImageIO.write(resizedImage, "jpg", file);
            return new Image(fileName, file.length());
        } else {
            log.warn("image null");
            return entityManager.merge(new Image(1));
        }
    }


}