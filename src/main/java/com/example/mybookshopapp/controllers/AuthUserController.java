package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.model.book.Book;
import com.example.mybookshopapp.security.model.ContactConfirmationPayload;
import com.example.mybookshopapp.security.model.ContactConfirmationResponse;
import com.example.mybookshopapp.security.model.RegistrationForm;
import com.example.mybookshopapp.service.UserProfileService;
import com.example.mybookshopapp.service.UserRegisterService;
import com.example.mybookshopapp.util.RegFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthUserController extends ModelAttributeController {

    private final UserRegisterService userRegister;
    private final RegFormValidator userValidator;

    @Autowired
    public AuthUserController(UserRegisterService userRegister,
                              RegFormValidator userValidator, UserProfileService userProfileService) {
        super(userProfileService);
        this.userRegister = userRegister;
        this.userValidator = userValidator;
    }

    @GetMapping("/signin")
    public String signinPage() {
        return "signin";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/registration")
    public String registrationNewUser(@ModelAttribute("regForm") @Valid RegistrationForm registrationForm,
                                      BindingResult bindingResult,
                                      Model model) {
        userValidator.validate(registrationForm, bindingResult);

        if (bindingResult.hasErrors())
            return "/signup";

        userRegister.registerUser(registrationForm);
        model.addAttribute("regOk", true);
        return "signin";
    }

    @GetMapping("/my")
    public String myPage() {
        return "my";
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        model.addAttribute("currentUser", userProfileService.getCurrentUser());
        return "profile";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload,
                                                   HttpServletResponse httpServletResponse) {
        ContactConfirmationResponse loginResponse = userRegister.jwtLogin(payload);
        Cookie cookie = new Cookie("token", loginResponse.getResult());
        httpServletResponse.addCookie(cookie);
        return loginResponse;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("searchResult")
    public List<Book> searchResult() {
        return new ArrayList<>();
    }
}
