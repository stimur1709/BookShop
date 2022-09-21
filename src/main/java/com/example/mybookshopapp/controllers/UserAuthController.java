package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.security.model.ContactConfirmationPayload;
import com.example.mybookshopapp.security.model.ContactConfirmationResponse;
import com.example.mybookshopapp.security.model.RegistrationForm;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.UserAuthService;
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

@Controller
public class UserAuthController extends ModelAttributeController {

    private final UserRegisterService userRegister;
    private final RegFormValidator userValidator;
    private final UserAuthService userAuthService;

    @Autowired
    public UserAuthController(UserRegisterService userRegister, RegFormValidator userValidator,
                              UserProfileService userProfileService, BookShopService bookShopService,
                              UserAuthService userAuthService) {
        super(userProfileService, bookShopService);
        this.userRegister = userRegister;
        this.userValidator = userValidator;
        this.userAuthService = userAuthService;
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
        //TODO реализовать отправку кода на телефон
        return userAuthService.handleRequestContactConfirmation(payload);
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        return new ContactConfirmationResponse(true);
    }

    @PostMapping("/registration")
    public String registrationNewUser(@ModelAttribute("regForm") @Valid RegistrationForm registrationForm,
                                      BindingResult bindingResult, Model model,
                                      @CookieValue(name = "cartContent", required = false) String cartContent,
                                      @CookieValue(name = "keptContent", required = false) String keptContent) {
        userValidator.validate(registrationForm, bindingResult);

        if (bindingResult.hasErrors())
            return "/signup";

        userRegister.registerUser(registrationForm, cartContent, keptContent);
        model.addAttribute("regOk", true);
        return "signin";
    }

    @GetMapping("/my")
    public String myPage() {
        return "my";
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        model.addAttribute("currentUser", getUserProfileService().getCurrentUser());
        return "profile";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload,
                                                   HttpServletResponse httpServletResponse) {
        ContactConfirmationResponse loginResponse = userAuthService.jwtLogin(payload);
        if (loginResponse.isResult() && loginResponse.getError() == null) {
            Cookie cookie = new Cookie("token", loginResponse.getToken());
            httpServletResponse.addCookie(cookie);
        }
        return loginResponse;
    }
}
