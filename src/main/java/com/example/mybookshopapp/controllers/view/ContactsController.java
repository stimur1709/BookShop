package com.example.mybookshopapp.controllers.view;

import com.example.mybookshopapp.data.dto.MessageDto;
import com.example.mybookshopapp.errors.DefaultException;
import com.example.mybookshopapp.service.news.BookShopService;
import com.example.mybookshopapp.service.news.MessageService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import com.example.mybookshopapp.util.MessageLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ContactsController extends ViewControllerImpl {

    private final MessageService messageService;

    @Autowired
    protected ContactsController(UserProfileService userProfileService, HttpServletRequest request,
                                 BookShopService bookShopService, MessageLocale messageLocale,
                                 MessageService messageService) {
        super(userProfileService, request, bookShopService, messageLocale);
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
    public String sendMessage(@ModelAttribute MessageDto messageDto, RedirectAttributes redirectAttributes) throws DefaultException {
        messageService.save(messageDto);
        String message = messageLocale.getMessage("message.sendMessage");
        redirectAttributes.addFlashAttribute("sendMessage", message);
        return "redirect:/contacts";
    }
}
