package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.*;
import com.example.mybookshopapp.model.user.UserLoginHistory;
import com.example.mybookshopapp.service.*;
import com.example.mybookshopapp.service.userService.UserAuthService;
import com.example.mybookshopapp.service.userService.UserChangeService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import com.example.mybookshopapp.service.userService.UserRegisterService;
import com.example.mybookshopapp.util.FormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
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
import java.util.List;
import java.util.Map;

@Controller
public class UserAuthController extends ModelAttributeController {

    private final UserRegisterService userRegisterService;
    private final UserAuthService userAuthService;
    private final UserChangeService userChangeService;
    private final FormValidator formValidator;
    private final HttpServletRequest request;
    private final UserContactService userContactService;
    private final UserLoginHistoryService userLoginHistoryService;

    @Autowired
    public UserAuthController(UserRegisterService userRegisterService, UserProfileService userProfileService,
                              BookShopService bookShopService, UserAuthService userAuthService,
                              UserChangeService userChangeService, FormValidator formValidator, MessageSource messageSource,
                              LocaleResolver localeResolver, HttpServletRequest request, UserContactService userContactService,
                              UserLoginHistoryService userLoginHistoryService) {
        super(userProfileService, bookShopService, messageSource, localeResolver);
        this.userRegisterService = userRegisterService;
        this.userAuthService = userAuthService;
        this.userChangeService = userChangeService;
        this.formValidator = formValidator;
        this.request = request;
        this.userContactService = userContactService;
        this.userLoginHistoryService = userLoginHistoryService;
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
    public ContactConfirmationResponse handlerApproveContact(@RequestBody ContactConfirmationPayload payload) {
        return userAuthService.handlerApproveContact(payload);
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
        Page<UserLoginHistory> page = userLoginHistoryService.getPageLoginHistory(0, 5);
        model.addAttribute("loginStories", page.getContent());
        model.addAttribute("show", page.getTotalPages() > 1);
        model.addAttribute("totalPages", page.getTotalPages());
        return "profile";
    }

    @GetMapping("/api/loginStory")
    @ResponseBody
    public List<UserLoginHistory> getPageLoginHistory(@RequestParam("page") int page,
                                                      @RequestParam("size") int size) {
        return userLoginHistoryService.getPageLoginHistory(page, size).getContent();
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

    @PostMapping("/api/profile/save")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveProfile(@RequestBody @Valid ChangeProfileForm changeProfileForm,
                                                           BindingResult bindingResult) {
        formValidator.validate(changeProfileForm, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, Object> response = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors())
                response.put(fieldError.getField(), fieldError.getDefaultMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = userChangeService.updateUser(changeProfileForm);

        if (response.isEmpty())
            response.put("message", messageSource.getMessage("message.profileSave", null, localeResolver.resolveLocale(request)));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/profile/cancel")
    @ResponseBody
    public ResponseEntity<?> cancelProfile() {
        userContactService.deleteAllNoApprovedUserContactByUser();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/profile")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getProfile() {
        Map<String, Object> response = new HashMap<>();
        response.put("user", userProfileService.getCurrentUserDTO());
        response.put("message", messageSource.getMessage("message.dataResumed", null, localeResolver.resolveLocale(request)));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
