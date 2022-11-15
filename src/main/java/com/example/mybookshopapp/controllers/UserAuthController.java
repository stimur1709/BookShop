package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.*;
import com.example.mybookshopapp.service.*;
import com.example.mybookshopapp.service.userService.UserAuthService;
import com.example.mybookshopapp.service.userService.UserChangeService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import com.example.mybookshopapp.service.userService.UserRegisterService;
import com.example.mybookshopapp.util.FormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserAuthController extends ModelAttributeController {

    private final UserRegisterService userRegisterService;
    private final UserAuthService userAuthService;
    private final UserChangeService userChangeService;
    private final FormValidator formValidator;

    private final UserContactService userContactService;

    @Autowired
    public UserAuthController(UserRegisterService userRegisterService, UserProfileService userProfileService,
                              BookShopService bookShopService, UserAuthService userAuthService,
                              UserChangeService userChangeService, FormValidator formValidator, MessageSource messageSource,
                              LocaleResolver localeResolver, UserContactService userContactService) {
        super(userProfileService, bookShopService, messageSource, localeResolver);
        this.userRegisterService = userRegisterService;
        this.userAuthService = userAuthService;
        this.userChangeService = userChangeService;
        this.formValidator = formValidator;
        this.userContactService = userContactService;
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
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload,
                                                                        HttpServletRequest request) {
        //TODO реализовать отправку кода на телефон
        return userAuthService.handlerRequestContactConfirmation(payload, request);
    }

    @PostMapping("/api/requestNewContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestNewContactConfirmation(@RequestBody ContactConfirmationPayload payload,
                                                                           HttpServletRequest request) {
        //TODO реализовать отправку кода на телефон
        return userRegisterService.handlerRequestNewContactConfirmation(payload, request);
    }

    @PostMapping("/api/requestChangeContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestChangeContactConfirmation(@RequestBody ContactConfirmationPayload payload,
                                                                              HttpServletRequest request) {
        //TODO реализовать отправку кода на телефон
        return userChangeService.handlerRequestChangeContactConfirmation(payload, request);
    }

    @PostMapping("/api/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handlerApproveContact(@RequestBody ContactConfirmationPayload payload,
                                                             HttpServletRequest request) {
        return userAuthService.handlerApproveContact(payload, request);
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
        model.addAttribute("currentUser", userProfileService.getCurrentUserDTO());
        return "profile";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload,
                                                   HttpServletResponse httpServletResponse, HttpServletRequest request) {
        ContactConfirmationResponse loginResponse = userAuthService.jwtLogin(payload, request);
        if (loginResponse.isResult() && loginResponse.getError() == null) {
            Cookie cookie = new Cookie("token", loginResponse.getToken());
            httpServletResponse.addCookie(cookie);
        }
        return loginResponse;
    }

    @PostMapping("/api/profile/save")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveProfile(HttpServletRequest request,
                                                           @RequestBody @Valid ChangeProfileForm changeProfileForm,
                                                           BindingResult bindingResult) {
        formValidator.validate(changeProfileForm, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, Object> response = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors())
                response.put(fieldError.getField(), fieldError.getDefaultMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = userChangeService.updateUser(changeProfileForm, request);

        if (response.isEmpty())
            response.put("message", messageSource.getMessage("message.profileSave", null, localeResolver.resolveLocale(request)));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/profile/cancel")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cancelProfile(HttpServletRequest request) {
        //TODO реализовать удаление неподтвержденной почты и номера телефона
        Map<String, Object> response = new HashMap<>();
        UserDto userDto = userContactService.deleteAllNoApprovedUserContactByUser();
        response.put("user", userDto);
        response.put("message", messageSource.getMessage("message.dataResumed", null, localeResolver.resolveLocale(request)));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
