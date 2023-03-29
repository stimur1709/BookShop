package com.example.mybookshopapp.controllers.view;

import com.example.mybookshopapp.data.dto.BookQuery;
import com.example.mybookshopapp.service.news.BookServiceImpl;
import com.example.mybookshopapp.service.news.TagServiceImpl;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class MainViewControllerImpl extends ViewControllerImpl {

    private final TagServiceImpl tagServiceImpl;
    private final BookServiceImpl bookServiceImpl;

    @Autowired
    protected MainViewControllerImpl(UserProfileService userProfileService, HttpServletRequest request, TagServiceImpl tagServiceImpl, BookServiceImpl bookServiceImpl) {
        super(userProfileService, request);
        this.tagServiceImpl = tagServiceImpl;
        this.bookServiceImpl = bookServiceImpl;
    }


    @GetMapping
    public String getPage(Model model) {
        model.addAttribute("recommendBooks", bookServiceImpl.getContents(new BookQuery(0, 6, "recommend", false)));
        model.addAttribute("recentBooks", bookServiceImpl.getContents(new BookQuery(0, 6, "pub_date", false)));
        model.addAttribute("popularBooks", bookServiceImpl.getContents(new BookQuery(0, 6, "popularity", false)));
        model.addAttribute("recentlyViewed", bookServiceImpl.getContents(new BookQuery(0, 6, "viewed", false)));
        model.addAttribute("tagsBooks", tagServiceImpl.getPageOfTagsBooks());
        return "index";
    }
}
