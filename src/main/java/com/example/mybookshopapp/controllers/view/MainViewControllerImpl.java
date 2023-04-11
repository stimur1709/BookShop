package com.example.mybookshopapp.controllers.view;

import com.example.mybookshopapp.data.dto.book.BooksFDto;
import com.example.mybookshopapp.data.outher.SearchWordDto;
import com.example.mybookshopapp.data.query.BookQuery;
import com.example.mybookshopapp.errors.DefaultException;
import com.example.mybookshopapp.service.BookServiceImpl;
import com.example.mybookshopapp.service.BookShopService;
import com.example.mybookshopapp.service.TagServiceImpl;
import com.example.mybookshopapp.service.userService.UserProfileService;
import com.example.mybookshopapp.util.MessageLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class MainViewControllerImpl extends ViewControllerImpl {

    private final TagServiceImpl tagServiceImpl;
    private final BookServiceImpl bookService;

    @Autowired
    protected MainViewControllerImpl(UserProfileService userProfileService, HttpServletRequest request,
                                     TagServiceImpl tagServiceImpl, BookServiceImpl bookService,
                                     BookShopService bookShopService, MessageLocale messageLocale) {
        super(userProfileService, request, bookShopService, messageLocale);
        this.tagServiceImpl = tagServiceImpl;
        this.bookService = bookService;
    }


    @GetMapping
    public String getPage(Model model) {
        model.addAttribute("recommendBooks", bookService.getContents(new BookQuery(0, 6, "recommend", false)));
        model.addAttribute("recentBooks", bookService.getContents(new BookQuery(0, 6, "pub_date", false)));
        model.addAttribute("popularBooks", bookService.getContents(new BookQuery(0, 6, "popularity", false)));
        model.addAttribute("recentlyViewed", bookService.getContents(new BookQuery(0, 6, "viewed", false)));
        model.addAttribute("tagsBooks", tagServiceImpl.getPageOfTagsBooks());
        return "index";
    }

    @GetMapping(value = {"/search/{searchWord}", "/search"})
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                  Model model) throws DefaultException {
        if (searchWordDto != null) {
            Page<BooksFDto> books = bookService.getContents(new BookQuery(0, 20, "pub_date", false, searchWordDto.getExample()));
            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("books", books);
            return "search/index";
        } else {
            String message = messageLocale.getMessage("message.searchRequest");
            throw new DefaultException(message);
        }
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/faq")
    public String faqPage() {
        return "faq";
    }
}
