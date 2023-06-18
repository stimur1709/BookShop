package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.ImageDto;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.service.ImageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/image")
public class RestImageControllerImpl
        extends RestDataControllerImpl<Query, ImageDto, ImageDto, ImageServiceImpl> {

    @Autowired
    public RestImageControllerImpl(ImageServiceImpl service) {
        super(service);
    }

    @PostMapping(value = "/file")
    public ResponseEntity<?> save(@RequestParam(name = "file", required = false) MultipartFile... file) {
        try {
            return new ResponseEntity<>(service.saveImage(file), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

}
