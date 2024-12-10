package com.deepanshu.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deepanshu.exception.ProductException;
import com.deepanshu.exception.UserException;
import com.deepanshu.modal.Review;
import com.deepanshu.modal.User;
import com.deepanshu.request.ReviewRequest;
import com.deepanshu.service.ReviewService;
import com.deepanshu.service.UserService;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "https://localhost:8081")
public class ReviewController {

    private ReviewService reviewService;
    private UserService userService;

    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Review> createReviewHandler(@RequestBody ReviewRequest req, @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        System.out.println("product id " + req.getProductId() + " - " + req.getReview());
        Review review = reviewService.createReview(req, user);
        System.out.println("product review " + req.getReview());
        return new ResponseEntity<Review>(review, HttpStatus.ACCEPTED);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<List<Review>> getProductsReviewHandler(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getAllReview(productId);
        return new ResponseEntity<List<Review>>(reviews, HttpStatus.OK);
    }

}
