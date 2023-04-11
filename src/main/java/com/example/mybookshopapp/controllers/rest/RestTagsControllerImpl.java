package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.tag.TagBookDto;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.service.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tags")
public class RestTagsControllerImpl
        extends RestDataControllerImpl<Query, TagBookDto, TagBookDto, TagServiceImpl> {

    @Autowired
    public RestTagsControllerImpl(TagServiceImpl service) {
        super(service);
    }

}
