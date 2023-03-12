package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.dto.MessageDto;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.MessageService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ContactsController extends ModelAttributeController {

    private final MessageService messageService;

    @Autowired
    public ContactsController(UserProfileService userProfileService,
                              BookShopService bookShopService, MessageSource messageSource, LocaleResolver localeResolver,
                              MessageService messageService, HttpServletRequest request) {
        super(userProfileService, bookShopService, messageSource, localeResolver, request);
        this.messageService = messageService;
    }

    @GetMapping("/contacts")
    public String contactsPage(Model model) {
        MessageDto message = userProfileService.isAuthenticatedUser()
                ? new MessageDto(userProfileService.getCurrentUserDTO().getLastname(), userProfileService.getCurrentUserDTO().getMail())
                : new MessageDto();
        model.addAttribute("message", message);
        return "contacts";
    }

    @PostMapping("/contacts")
    public String sendMessage(@ModelAttribute MessageDto messageDto, RedirectAttributes redirectAttributes) {
        messageService.sendMessage(messageDto);
        String message = messageSource.getMessage("message.sendMessage", null, localeResolver.resolveLocale(request));
        redirectAttributes.addFlashAttribute("sendMessage", message);
        return "redirect:/contacts";
    }
}
