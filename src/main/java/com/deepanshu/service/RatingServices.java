package com.deepanshu.service;

import java.util.List;

import com.deepanshu.exception.ProductException;
import com.deepanshu.modal.Rating;
import com.deepanshu.modal.User;
import com.deepanshu.request.RatingRequest;

public interface RatingServices {
    // Create rating for any product
    Rating createRatingForProduct(RatingRequest ratingRequest);

    public Rating createRating(RatingRequest req, User user) throws ProductException;

    public List<Rating> getProductsRating(Long productId);
}
