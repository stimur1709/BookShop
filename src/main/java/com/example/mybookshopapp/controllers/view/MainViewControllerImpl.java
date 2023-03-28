package com.example.mybookshopapp.controllers.view;

import com.example.mybookshopapp.data.dto.BookQuery;
import com.example.mybookshopapp.data.dto.BooksFDto;
import com.example.mybookshopapp.data.entity.news.BooksF;
import com.example.mybookshopapp.service.TagService;
import com.example.mybookshopapp.service.news.BookService;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainViewControllerImpl extends ViewControllerImpl<BooksF, BookQuery, BooksFDto, BookService> {

    private final TagService tagService;

    @Autowired
    protected MainViewControllerImpl(UserProfileService userProfileService, BookService service, TagService tagService) {
        super("index", userProfileService, service);
        this.tagService = tagService;
    }

    @Override
    public String getPage(Model model) {
        model.addAttribute("recommendBooks", service.getContents(new BookQuery(0, 6, "recommend", false)));
        model.addAttribute("recentBooks", service.getContents(new BookQuery(0, 6, "pub_date", false)));
        model.addAttribute("popularBooks", service.getContents(new BookQuery(0, 6, "popularity", false)));
        model.addAttribute("recentlyViewed", service.getContents(new BookQuery(0, 6, "viewed", false)));
        model.addAttribute("tagsBooks", tagService.getPageOfTagsBooks());
        return super.getPage(model);
    }
}
