package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.data.dto.BookReviewDto;
import com.example.mybookshopapp.data.outher.ReviewLikeDto;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.service.BookRateReviewService;
import com.example.mybookshopapp.service.BookReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/review")
public class RestBookReviewControllerImpl
        extends RestDataControllerImpl<Query, BookReviewDto, BookReviewDto, BookReviewServiceImpl> {

    private final BookRateReviewService bookRateReviewService;

    @Autowired
    public RestBookReviewControllerImpl(BookReviewServiceImpl service, BookRateReviewService bookRateReviewService) {
        super(service);
        this.bookRateReviewService = bookRateReviewService;
    }

    @PostMapping("/rateBookReview")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> rateBookReview(@RequestBody ReviewLikeDto reviewLikeDto) {
        Map<String, Object> response = bookRateReviewService.changeRateBookReview(reviewLikeDto.getReviewid(), reviewLikeDto.getValue());
        return ok(response);
    }

    @GetMapping("/unconfirmed")
    public ResponseEntity<Long> getUnconfirmedReview() {
        return ok(service.getUnconfirmedReview());
    }

}
