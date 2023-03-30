package com.example.mybookshopapp.controllers.view;

import com.example.mybookshopapp.service.news.BookShopService;
import com.example.mybookshopapp.service.news.DocumentService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import com.example.mybookshopapp.util.MessageLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DocumentsController extends ViewControllerImpl {

    private final DocumentService documentService;

    @Autowired
    protected DocumentsController(UserProfileService userProfileService, HttpServletRequest request, BookShopService bookShopService, MessageLocale messageLocale, DocumentService documentService) {
        super(userProfileService, request, bookShopService, messageLocale);
        this.documentService = documentService;
    }

    @GetMapping("/documents")
    public String documentsPage(Model model) {
        model.addAttribute("documents",
                documentService.getAllContents(Sort.by(Sort.Direction.DESC, "sortIndex")));
        return "documents/index";
    }

    @GetMapping("/documents/{slug}")
    public String documentPage(@PathVariable("slug") String slug, Model model) {
        model.addAttribute("document", documentService.getContent(slug));
        return "documents/slug";
    }
}
