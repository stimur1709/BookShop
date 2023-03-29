package com.example.mybookshopapp.controllers.view;

import com.example.mybookshopapp.data.dto.BookQuery;
import com.example.mybookshopapp.data.dto.BooksFDto;
import com.example.mybookshopapp.service.news.BookServiceImpl;
import com.example.mybookshopapp.service.news.TagServiceImpl;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/tags")
public class TagPageController extends ViewControllerImpl {

    private final TagServiceImpl tagService;
    private final BookServiceImpl bookService;

    @Autowired
    protected TagPageController(UserProfileService userProfileService, HttpServletRequest request, TagServiceImpl tagService, BookServiceImpl bookService) {
        super(userProfileService, request);
        this.tagService = tagService;
        this.bookService = bookService;
    }

    @GetMapping("/{slug}")
    public String tagPage(@PathVariable(value = "slug") String slug, Model model) {
        Page<BooksFDto> books = bookService.getContents(new BookQuery(0, 20, "tag", slug));
        model.addAttribute("content", tagService.getContent(slug));
        model.addAttribute("booksTag", books);
        model.addAttribute("show", books.getTotalPages() > 1);
        model.addAttribute("totalPages", books.getTotalPages());
        return "tags/index";
    }

}