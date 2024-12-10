package com.deepanshu.controller;

import com.deepanshu.exception.ProductException;
import com.deepanshu.modal.Rating;
import com.deepanshu.request.RatingRequest;
import com.deepanshu.service.RatingServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingServices ratingService;

    // Endpoint to create or update a rating for a product
    @PostMapping("/create")
    public ResponseEntity<Rating> createRating(@RequestBody RatingRequest ratingRequest) {
        try {
            Rating createdRating = ratingService.createRatingForProduct(ratingRequest);
            return new ResponseEntity<>(createdRating, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint to get ratings for a specific product
    @GetMapping("/{productId}")
    public ResponseEntity<List<Rating>> getProductRatings(@PathVariable Long productId) {
        List<Rating> ratings = ratingService.getProductsRating(productId);
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }
}
