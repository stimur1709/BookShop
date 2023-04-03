package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.errors.DefaultException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(DefaultException.class)
    public String handlerEmptySearchException(DefaultException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("searchError", e);
        return "redirect:/";
    }
}
