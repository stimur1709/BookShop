package com.example.mybookshopapp.controllers.view;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public interface ViewController {

    @GetMapping
    String getPage(Model model);

}
