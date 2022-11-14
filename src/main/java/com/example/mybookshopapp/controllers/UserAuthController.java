package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.ContactConfirmationResponse;
import com.example.mybookshopapp.dto.RegistrationForm;
import com.example.mybookshopapp.service.*;
import com.example.mybookshopapp.service.userService.UserAuthService;
import com.example.mybookshopapp.service.userService.UserChangeService;
import com.example.mybookshopapp.service.UserProfileService;
import com.example.mybookshopapp.service.userService.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserAuthController extends ModelAttributeController {

    private final UserRegisterService userRegisterService;
    private final UserAuthService userAuthService;
    private final UserChangeService userChangeService;

    @Autowired
    public UserAuthController(UserRegisterService userRegisterService, UserProfileService userProfileService,
                              BookShopService bookShopService, UserAuthService userAuthService,
                              UserChangeService userChangeService) {
        super(userProfileService, bookShopService);
        this.userRegisterService = userRegisterService;
        this.userAuthService = userAuthService;
        this.userChangeService = userChangeService;
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

    @PostMapping("/api/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload) {
        //TODO реализовать отправку кода на телефон
        return userAuthService.handlerRequestContactConfirmation(payload);
    }

    @PostMapping("/api/requestNewContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestNewContactConfirmation(@RequestBody ContactConfirmationPayload payload) {
        //TODO реализовать отправку кода на телефон
        return userRegisterService.handlerRequestNewContactConfirmation(payload);
    }

    @PostMapping("/api/requestChangeContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestChangeContactConfirmation(@RequestBody ContactConfirmationPayload payload) {
        //TODO реализовать отправку кода на телефон
        return userChangeService.handlerRequestChangeContactConfirmation(payload);
    }

    @PostMapping("/api/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handlerApproveContact(@RequestBody ContactConfirmationPayload payload,
                                                             HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        ContactConfirmationResponse response = userAuthService.handlerApproveContact(payload);
        if (response.getToken() != null) {
            for (Cookie cookie : httpServletRequest.getCookies())
                if (cookie.getName().equals("token")) {
                    cookie.setValue(response.getToken());
                    cookie.setPath("/");
                    httpServletResponse.addCookie(cookie);
                }
        }
        return response;
    }

    @PostMapping("/registration")
    public String registrationNewUser(@ModelAttribute("regForm") RegistrationForm registrationForm, Model model,
                                      @CookieValue(name = "cartContent", required = false) String cartContent,
                                      @CookieValue(name = "keptContent", required = false) String keptContent) {
        userRegisterService.registerUser(registrationForm, cartContent, keptContent);
        model.addAttribute("regOk", true);
        return "signin";
    }

    @GetMapping("/my")
    public String myPage() {
        return "my";
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        model.addAttribute("currentUser", getUserProfileService().getCurrentUserDTO());
        return "profile";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload,
                                                   HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
        ContactConfirmationResponse loginResponse = userAuthService.jwtLogin(payload, httpServletRequest);
        if (loginResponse.isResult() && loginResponse.getError() == null) {
            Cookie cookie = new Cookie("token", loginResponse.getToken());
            httpServletResponse.addCookie(cookie);
        }
        return loginResponse;
    }

    @PostMapping("/profile/save")
    public String saveProfile(/*@ModelAttribute("currentUser") RegistrationForm registrationForm*/) {
        System.out.println(123);
        return "profile";
    }

    @PostMapping("/profile/cancel")
    public String cancelProfile() {
        return "profile";
    }

}
