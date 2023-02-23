package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.DocumentService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.LocaleResolver;

@Controller
public class DocumentsController extends ModelAttributeController {

    private final DocumentService documentService;

    @Autowired
    public DocumentsController(UserProfileService userProfileService, BookShopService bookShopService, MessageSource messageSource, LocaleResolver localeResolver, DocumentService documentService) {
        super(userProfileService, bookShopService, messageSource, localeResolver);
        this.documentService = documentService;
    }

    @GetMapping("/documents")
    public String documentsPage() {
        return "documents/index";
    }

    @GetMapping("/documents/{slug}")
    public String documentPage(@PathVariable("slug") String slug, Model model) {
        model.addAttribute("document", documentService.getDocument(slug));
        return "documents/slug";
    }
}
